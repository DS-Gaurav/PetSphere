import requests
import pandas as pd
import time
import os
import re
from urllib.parse import quote

BASE_URL = "https://headsupfortails.com"
HEADERS = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
}

# Cat-only collections
COLLECTIONS = {
    "Grain Free":"grain-free",
    "Veterinary Food":"veterinary-food",
    "Cat Dry Food": "cat-dry-food",
    "Cat Wet Food": "cat-wet-food",
    "Gravy": "gravy",
    "Broth": "broth",
    "Kitten Food": "kitten-food",
    "Supplements": "cat-supplements",
    "Mousse": "mousse",
    "Pate": "pate"
}

def fetch_products(handle, page):
    url = f"{BASE_URL}/collections/{handle}/products.json?page={page}"
    try:
        res = requests.get(url, headers=HEADERS)
        if res.status_code != 200:
            print(f"❌ Failed page {page} of {handle} - {res.status_code}")
            return []
        return res.json().get("products", [])
    except Exception as e:
        print(f"⚠️ Error on {handle} page {page}: {e}")
        return []

def sanitize_filename(name):
    return re.sub(r'[\\/*?:"<>|]', "", name)

def download_image(image_url, title):
    try:
        if not image_url:
            return ""
        os.makedirs("cat_images", exist_ok=True)
        filename = sanitize_filename(title)[:50] + ".jpg"
        path = os.path.join("cat_images", filename)
        response = requests.get(image_url, stream=True)
        if response.status_code == 200:
            with open(path, 'wb') as f:
                f.write(response.content)
            return path
    except Exception as e:
        print(f"⚠️ Image download failed for {title}: {e}")
    return ""

all_products = []

for category, handle in COLLECTIONS.items():
    page = 1
    print(f"\n🔍 Scraping category: {category}")
    while True:
        print(f"  📦 Fetching page {page} for {handle}")
        products = fetch_products(handle, page)
        if not products:
            break

        for p in products:
            title = p["title"].lower()
            tags = [tag.lower() for tag in p.get("tags", [])]

            # Filter: skip dog or non-cat related products
            if "dog" in title or any(tag in tags for tag in ["dog", "canine"]):
                continue
            if not ("cat" in title or "kitten" in title or any(tag in tags for tag in ["cat", "kitten", "feline"])):
                continue

            image_url = ""
            local_img_path = ""
            if p["images"]:
                img_src = p["images"][0]["src"]
                image_url = "https:" + img_src if img_src.startswith("//") else img_src
                local_img_path = download_image(image_url, p["title"])

            all_products.append({
                "name": p["title"],
                "price": p["variants"][0]["price"],
                "url": BASE_URL + "/products/" + p["handle"],
                "image_url": image_url,
                "image_path": local_img_path,
                "category": category,
                "pet_type": "Cat"
            })

        page += 1
        time.sleep(1)

# Save to CSV
if all_products:
    df = pd.DataFrame(all_products)
    df.to_csv("cat_food_shopify_products.csv", index=False)
    print("\n✅ Cat food scraping complete! Data saved to cat_food_shopify_products.csv")
else:
    print("\n❌ No cat food products found.")
