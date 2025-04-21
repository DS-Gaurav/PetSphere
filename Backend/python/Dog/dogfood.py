import os
import time
import requests
import pandas as pd
from urllib.parse import urlparse

BASE_URL = "https://headsupfortails.com"
COLLECTIONS = {
    "Dry Food":"dry-food",
    "Wet Food":"wet-food",
    "Daily Meals": "daily-meals",
    "Grain Free": "grain-free",
    "Puppy Food": "puppy-food",
    "Hypoallergenic": "hypoallergenic",
    "Veterinary Food": "veterinary-food",
    "Food Toppers & Gravy": "food-toppers-and-gravy",
    "Chicken Free": "chicken-free",
    "Supplements": "supplements"
}
HEADERS = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
}

# Directory to save images
IMAGE_DIR = "dog_food_images"
os.makedirs(IMAGE_DIR, exist_ok=True)

all_products = []

# Function to fetch products from a collection and page
def fetch_products(collection_handle, page=1):
    url = f"{BASE_URL}/collections/{collection_handle}/products.json?page={page}"
    try:
        res = requests.get(url, headers=HEADERS, timeout=10)
        res.raise_for_status()
        return res.json().get("products", [])
    except requests.RequestException as e:
        print(f"❌ Error fetching page {page} of '{collection_handle}': {e}")
        return []

# Function to download image locally and return its path
def download_image(img_url, product_name):
    if not img_url:
        return ""
    try:
        ext = os.path.splitext(urlparse(img_url).path)[1]
        safe_name = "".join(c if c.isalnum() else "_" for c in product_name)
        filename = f"{safe_name[:50]}{ext}"
        filepath = os.path.join(IMAGE_DIR, filename)

        if not os.path.exists(filepath):
            img_data = requests.get(img_url, headers=HEADERS, timeout=10).content
            with open(filepath, 'wb') as f:
                f.write(img_data)
        return filepath
    except Exception as e:
        print(f"⚠️ Failed to download image for {product_name}: {e}")
        return ""

# Main loop through all categories
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

            # 🚫 Skip cat/kitten/feline products
            if "cat" in title or "kitten" in title or any(tag in tags for tag in ["cat", "kitten", "feline"]):
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
                "pet_type": "Dog"
            })
        page += 1
        time.sleep(1)


# Save to CSV
if all_products:
    df = pd.DataFrame(all_products)
    df.to_csv("all_dog_food_products_with_images.csv", index=False)
    print("\n✅ Done! Data saved to all_dog_food_products_with_images.csv and images saved to 'dog_food_images/' folder.")
else:
    print("\n❌ No products were scraped.")
