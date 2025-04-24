import pandas as pd
import os

# Load CSV once
csv_path = os.path.join("product_data", "food_products.csv")
df = pd.read_csv(csv_path)

def get_product_recommendation(pet_type, age_group, optional_filters=None):
    # Filter by pet_type and age (from category column)
    filtered_df = df[
        df["pet_type"].str.lower().str.contains(pet_type.lower(), na=False) &
        df["category"].str.lower().str.contains(age_group.lower(), na=False)
    ]

    # Apply optional filters if any
    if optional_filters:
        for filter_term in optional_filters:
            filtered_df = filtered_df[
                ~filtered_df["name"].str.lower().str.contains(filter_term.lower(), na=False) &
                ~filtered_df["category"].str.lower().str.contains(filter_term.lower(), na=False)
            ]

    if filtered_df.empty:
        return "Sorry, no matching products found for your pet."

    # Format top 3 results
    top_matches = filtered_df.head(3)
    response_lines = []

    for _, row in top_matches.iterrows():
        line = f"{row['name']} - ₹{row['price']}\n👉 [Buy Now]({row['url']})"
        response_lines.append(line)

    return "\n\n".join(response_lines)
