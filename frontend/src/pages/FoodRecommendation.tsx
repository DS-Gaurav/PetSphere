import React, { useState } from "react";
import Navbar from "../components/Navbar";
import axios from "axios";
import dogs from "../assets/dogs.jpg";

const FoodRecommendation = () => {
  const [petType, setPetType] = useState("cat");
  const [age, setAge] = useState("kitten");
  const [recommendations, setRecommendations] = useState([]);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      const response = await axios.get(
        `http://127.0.0.1:5000/recommend?pet_type=${petType}&age=${age}`
      );
      setRecommendations(response.data);
    } catch (error) {
      console.error("Error fetching recommendations:", error);
      alert("Failed to fetch food recommendations.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Navbar />
      <div
        className="bg-cover bg-center min-h-screen flex items-center justify-center"
        style={{
          backgroundImage: `url(${dogs})`,
        }}
      >
        <div className="max-w-xl mx-auto bg-white bg-opacity-90 p-6 rounded-lg shadow-md">
          <h2 className="text-2xl font-bold mb-4 text-center text-gray-800">
            Food Recommendation Form
          </h2>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block font-medium text-gray-700">
                Pet Type
              </label>
              <select
                value={petType}
                onChange={(e) => setPetType(e.target.value)}
                className="w-full border border-gray-300 rounded px-4 py-2"
              >
                <option value="dog">Dog</option>
                <option value="cat">Cat</option>
              </select>
            </div>
            <div>
              <label className="block font-medium text-gray-700">Age</label>
              <select
                value={age}
                onChange={(e) => setAge(e.target.value)}
                className="w-full border border-gray-300 rounded px-4 py-2"
              >
                <option value="kitten">Kitten</option>
                <option value="adult">Adult</option>
                <option value="puppy">Puppy</option>
              </select>
            </div>
            <button
              type="submit"
              className="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 rounded"
            >
              Get Recommendations
            </button>
          </form>
        </div>

        {/* Results */}
        <div className="max-w-5xl mx-auto mt-10 grid gap-6 grid-cols-1 md:grid-cols-2 lg:grid-cols-3">
          {loading && <p className="text-white col-span-full">Loading...</p>}
          {recommendations.map((item: any, index: number) => (
            <div
              key={index}
              className="bg-white rounded-lg shadow-md overflow-hidden"
            >
              <img
                src={item.image_url}
                alt={item.name}
                className="w-full h-48 object-cover"
              />
              <div className="p-4">
                <h3 className="text-lg font-bold text-gray-800">{item.name}</h3>
                <p className="text-sm text-gray-600 mt-1">{item.category}</p>
                <p className="text-sm text-gray-600">Type: {item.pet_type}</p>
                <p className="text-blue-600 font-semibold mt-2">
                  ₹ {item.price}
                </p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </>
  );
};

export default FoodRecommendation;
