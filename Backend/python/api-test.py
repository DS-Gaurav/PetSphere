from flask import Flask, request, jsonify
from flask_cors import CORS  # <-- ADD THIS LINE
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

app = Flask(__name__)
CORS(app)  # <-- ADD THIS LINE TO ENABLE CORS FOR ALL ROUTES

# Load CSV
df = pd.read_csv("food_products.csv")
df.fillna('', inplace=True)

# Preprocess price
def extract_price(price_str):
    try:
        return float(''.join(filter(str.isdigit, price_str)))
    except:
        return 9999

df['price_value'] = df['price'].apply(extract_price)

# Combine features for TF-IDF
df['combined'] = df['name'] + " " + df['category'] + " " + df['pet_type']

# TF-IDF vectorization
vectorizer = TfidfVectorizer(stop_words='english')
tfidf_matrix = vectorizer.fit_transform(df['combined'])

@app.route('/recommend', methods=['GET'])
def recommend():
    pet_type = request.args.get('pet_type', '').lower()
    age = request.args.get('age', '').lower()
    exclude = request.args.get('exclude', '').lower()
    category = request.args.get('category', '').lower()

    # Build user query string
    query = f"{pet_type} {age} {category}"
    query_vector = vectorizer.transform([query])

    # Compute cosine similarity
    cosine_sim = cosine_similarity(query_vector, tfidf_matrix).flatten()

    # Sort by similarity
    top_indices = cosine_sim.argsort()[::-1]

    recommendations = []
    for idx in top_indices:
        row = df.iloc[idx]
        if exclude and exclude in row['name'].lower():
            continue
        recommendations.append({
            'name': row['name'],
            'price': row['price'],
            'image_url': row['image_url'],
            'pet_type': row['pet_type'],
            'category': row['category'],
            'score': round(float(cosine_sim[idx]), 3)
        })
        if len(recommendations) == 10:
            break

    return jsonify(recommendations)

if __name__ == "__main__":
    print("🚀 Starting Flask API on http://127.0.0.1:5000")
    app.run(debug=True)
