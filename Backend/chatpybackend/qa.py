import os
import pandas as pd
import google.generativeai as genai
from dotenv import load_dotenv

load_dotenv()

# Load product data
data = pd.read_csv("product_data/food_products.csv")

# Set up Gemini
genai.configure(api_key="AIzaSyCQmal-eT6HN0F2qTgpY2rENCQ6QvquUio")
model = genai.GenerativeModel("gemini-1.5-flash-latest")

def get_answer(question):  # THIS FUNCTION is what app.py is trying to import
    question = question.lower()

    # CSV-based logic
    if "best food for" in question:
        pet_type = question.split("for")[-1].strip()
        filtered = data[data["pet_type"].str.lower() == pet_type]
        if not filtered.empty:
            top = filtered.iloc[0]
            return (f"Best food for {pet_type} is '{top['name']}' "
                    f"from category '{top['category']}' at ₹{top['price']}. "
                    f"Buy here: {top['url']}")

    elif "price of" in question:
        name = question.split("of")[-1].strip()
        filtered = data[data["name"].str.lower().str.contains(name)]
        if not filtered.empty:
            product = filtered.iloc[0]
            return f"The price of '{product['name']}' is ₹{product['price']}."

    elif "list" in question and "category" in question:
        category = question.split("category")[-1].strip()
        filtered = data[data["category"].str.lower().str.contains(category)]
        if not filtered.empty:
            names = filtered["name"].tolist()
            return f"Products in category '{category}':\n- " + "\n- ".join(names[:5])

    
    # Fallback to Gemini with added context
    try:
        prompt = (
            f"{question}"
            "You are a helpful assistant that specializes in pet care products. "
            "Answer the following user query in a clear and concise way. "
            "If the question is about pet food, grooming items, toys, or accessories, "
            "try to provide practical, useful advice. Here's the question"
            "Answer the following user question in 2-3 sentences max:\n\n"
            
        )
        response = model.generate_content(prompt)
        return response.text or "Sorry, I couldn't find an answer to that."
    except Exception as e:
        return f"Gemini error: {e}"
