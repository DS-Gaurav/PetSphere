import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";

const AddPet = () => {
  const [formData, setFormData] = useState({
    category: "",
    breed: "",
    age: "",
    price: "",
    image: null,
    report: null,
  });

  const navigate = useNavigate();
  const token = localStorage.getItem("token");

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
    formDataObj.append("files", formData.image);
    formDataObj.append("files", formData.mceretificate);
  
    try {
      await axios.post("http://localhost:9090/pets", formDataObj, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "multipart/form-data",
        },
      });
      alert("Pet added successfully!");
      navigate("/mypets");
    } catch (error) {
      console.error("Error:", error);
      alert("Failed to add pet.");
    }
  };

  return (
    <div className="min-h-screen bg-[url('/home/dsgaurav/Desktop/javatraining/petapp/frontend/src/assets/dogs.jpg')] bg-cover bg-center">
      <Navbar />
      <div className="max-w-lg mx-auto mt-12 bg-white/80 p-6 rounded-lg shadow-lg backdrop-blur-md">
        <h2 className="text-3xl font-bold text-gray-800 text-center mb-6">Add a New Pet</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-gray-700 font-semibold mb-1">Category</label>
            <input
              name="category"
              type="text"
              placeholder="Enter pet category"
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
              placeholder="Enter breed"
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
                placeholder="Enter age"
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
                placeholder="Enter price"
                required
                className="w-full p-3 border rounded-lg focus:ring focus:ring-blue-300"
                onChange={handleChange}
              />
            </div>
          </div>

          <div>
            <label className="block text-gray-700 font-semibold mb-1">Upload Image</label>
            <input
              name="image"
              type="file"
              accept="image/*"
              required
              className="w-full border p-3 rounded-lg bg-gray-50"
              onChange={handleFileChange}
            />
          </div>

          <div>
            <label className="block text-gray-700 font-semibold mb-1">Upload Report</label>
            <input
              name="report"
              type="file"
              required
              className="w-full border p-3 rounded-lg bg-gray-50"
              onChange={handleFileChange}
            />
          </div>

          <button
            type="submit"
            className="w-full bg-blue-600 text-white py-3 rounded-lg font-semibold hover:bg-blue-700 transition"
          >
            Submit
          </button>
        </form>
      </div>
    </div>
  );
};

export default AddPet;
