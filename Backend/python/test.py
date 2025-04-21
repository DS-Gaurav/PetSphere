import requests
import pandas as pd
import time

BASE_URL = "https://headsupfortails.com"
JSON_ENDPOINT = f"{BASE_URL}/collections/dog-food/products.json"
HEADERS = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
}

all_products = []

def fetch_products(page=1):
    url = f"{JSON_ENDPOINT}?page={page}"
    res = requests.get(url, headers=HEADERS)

    if res.status_code != 200:
        print(f"❌ Failed to fetch page {page}. Status code: {res.status_code}")
        return []

    data = res.json()
    return data.get("products", [])

page = 1
while True:
    print(f"📦 Fetching page {page}")
    products = fetch_products(page)
    if not products:
        break

    for p in products:
        image_url = ""
        if p["images"]:
            img_src = p["images"][0]["src"]
            image_url = "https:" + img_src if img_src.startswith("//") else img_src

        all_products.append({
            "name": p["title"],
            "price": p["variants"][0]["price"],
            "url": BASE_URL + "/products/" + p["handle"],
            "image": image_url,
            "category": "Dog Food",
            "pet_type": "Dog"
        })

    page += 1
    time.sleep(1)

# Save to CSV
if all_products:
    df = pd.DataFrame(all_products)
    df.to_csv("dog_food_shopify_products.csv", index=False)
    print("\n✅ Scraping complete! Data saved to dog_food_shopify_products.csv")
else:
    print("\n❌ No products were scraped, CSV not created.")
