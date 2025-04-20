import { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";
import Navbar from "../components/Navbar";

const EditPet = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  const [formData, setFormData] = useState({
    category: "",
    breed: "",
    age: "",
    price: "",
    image: null,
    report: null,
  });

  useEffect(() => {
    const fetchPetDetails = async () => {
      try {
        const response = await axios.get(`http://localhost:9090/pets/${id}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        const pet = response.data;
        setFormData({
          category: pet.category,
          breed: pet.breed,
          age: pet.age,
          price: pet.price,
          image: null,
          report: null,
        });
      } catch (error) {
        console.error("Error fetching pet details:", error);
        alert("Failed to load pet details.");
        navigate("/mypets");
      }
    };
    fetchPetDetails();
  }, [id, navigate, token]);

  const handleChange = (e: any) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleFileChange = (e: any) => {
    const { name, files } = e.target;
    setFormData((prev) => ({ ...prev, [name]: files[0] }));
  };

  const handleSubmit = async (e: any) => {
    e.preventDefault();
    const formDataObj = new FormData();

    const petData = {
      category: formData.category,
      breed: formData.breed,
      age: formData.age,
      price: formData.price,
    };

    formDataObj.append("pet", new Blob([JSON.stringify(petData)], { type: "application/json" }));
    if (formData.image) formDataObj.append("files", formData.image);
    if (formData.report) formDataObj.append("files", formData.report);

    try {
      await axios.put(`http://localhost:9090/pets/${id}`, formDataObj, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "multipart/form-data",
        },
      });
      alert("Pet updated successfully!");
      navigate("/mypets");
    } catch (error) {
      console.error("Error:", error);
      alert("Failed to update pet.");
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />
      <div className="max-w-lg mx-auto mt-12 bg-white p-6 rounded-lg shadow-lg">
        <h2 className="text-3xl font-bold text-gray-800 text-center mb-6">Edit Pet</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-gray-700 font-semibold mb-1">Category</label>
            <input
              name="category"
              type="text"
              value={formData.category}
              required
              className="w-full p-3 border rounded-lg focus:ring focus:ring-blue-300"
              onChange={handleChange}
            />
          </div>
          <div>
            <label className="block text-gray-700 font-semibold mb-1">Breed</label>
            <input
              name="breed"
              type="text"
              value={formData.breed}
              required
              className="w-full p-3 border rounded-lg focus:ring focus:ring-blue-300"
              onChange={handleChange}
            />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-gray-700 font-semibold mb-1">Age</label>
              <input
                name="age"
                type="number"
                value={formData.age}
                required
                className="w-full p-3 border rounded-lg focus:ring focus:ring-blue-300"
                onChange={handleChange}
              />
            </div>
            <div>
              <label className="block text-gray-700 font-semibold mb-1">Price (₹)</label>
              <input
                name="price"
                type="number"
                value={formData.price}
                required
                className="w-full p-3 border rounded-lg focus:ring focus:ring-blue-300"
                onChange={handleChange}
              />
            </div>
          </div>
          <div>
            <label className="block text-gray-700 font-semibold mb-1">Upload New Image</label>
            <input
              name="image"
              type="file"
              accept="image/*"
              className="w-full border p-3 rounded-lg bg-gray-50"
              onChange={handleFileChange}
            />
          </div>
          <div>
            <label className="block text-gray-700 font-semibold mb-1">Upload New Report</label>
            <input
              name="report"
              type="file"
              className="w-full border p-3 rounded-lg bg-gray-50"
              onChange={handleFileChange}
            />
          </div>
          <button
            type="submit"
            className="w-full bg-blue-600 text-white py-3 rounded-lg font-semibold hover:bg-blue-700 transition"
          >
            Update Pet
          </button>
        </form>
      </div>
    </div>
  );
};

export default EditPet;
