import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import Navbar from "../components/Navbar";

const PetDetails = () => {
  const { id } = useParams(); // Get pet ID from URL
  const [pet, setPet] = useState(null);
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  useEffect(() => {
    const fetchPetDetails = async () => {
      try {
        const response = await axios.get(`http://localhost:9090/pets/${id}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setPet(response.data);
      } catch (error) {
        console.error("Error fetching pet details:", error);
        alert("Failed to load pet details.");
        navigate("/mypets"); // Redirect back if error occurs
      }
    };

    fetchPetDetails();
  }, [id, navigate, token]);

  const handleSellToggle = async () => {
    try {
      await axios.post(`http://localhost:9090/pets/settrue/${id}`, {}, {
        headers: { Authorization: `Bearer ${token}` },
      });
      // Toggle sell state locally after API call success
      setPet((prevPet) => ({ ...prevPet, isForSale: !prevPet.isForSale }));
    } catch (error) {
      console.error("Error updating sell status:", error);
      alert("Failed to update sell status.");
    }
  };

  if (!pet) return <p className="text-center mt-12">Loading...</p>;

  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />
      <div className="max-w-3xl mx-auto mt-12 bg-white p-6 rounded-lg shadow-lg">
        <h2 className="text-3xl font-bold text-gray-800 text-center mb-6">
          {pet.category} Details
        </h2>
        <img
          src={pet.image}
          alt={pet.category}
          className="w-full h-64 object-cover rounded-lg mb-4"
        />
        <p className="text-lg"><strong>Breed:</strong> {pet.breed}</p>
        <p className="text-lg"><strong>Age:</strong> {pet.age} years</p>
        <p className="text-lg"><strong>Price:</strong> ₹{pet.price}</p>
        <p className="text-lg"><strong>Health Report:</strong></p>
        <a
          href={pet.mceretificate}
          target="_blank"
          rel="noopener noreferrer"
          className="text-blue-600 underline"
        >
          View Health Report
        </a>
        <div className="mt-6 flex gap-2 justify-end">
          <button
            onClick={() => navigate("/mypets")}
            className="bg-gray-600 text-white py-2 px-4 rounded-lg hover:bg-gray-700 transition"
          >
            Back to My Pets
          </button>
          <button
            onClick={() => navigate(`/editpet/${pet.id}`)}
            className="bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700 transition"
          >
            Edit Details
          </button>
          <button
            onClick={handleSellToggle}
            className={`py-2 px-4 rounded-lg transition ${
              pet.isForSale ? "bg-red-500 hover:bg-red-600" : "bg-green-500 hover:bg-green-600"
            } text-white`}
          >
            {pet.isForSale ? "Unsell" : "Sell"}
          </button>
        </div>
      </div>
    </div>
  );
};

export default PetDetails;
