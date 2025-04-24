import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import Navbar from "../components/Navbar";
import dogs from "../assets/dogs.jpg";

const Main = () => {
  const [pets, setPets] = useState([]); // Store pets data
  const [searchTerm, setSearchTerm] = useState(""); // Store search input
  const [isHovered, setIsHovered] = useState(false);
  const [page, setPage] = useState(0); // Track the current page
  const [totalPages, setTotalPages] = useState(1); // Total pages from API
  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const [chatInput, setChatInput] = useState(""); // Store chatbot input
  const [chatResponse, setChatResponse] = useState(""); // Store chatbot response

  const handleAskAI = async () => {
    try {
      const response = await axios.post("http://127.0.0.1:5001/ask", {
        question: chatInput,
      });
      setChatResponse(response.data.answer);
    } catch (error) {
      console.error("Error fetching AI response:", error);
      setChatResponse("Something went wrong. Please try again later.");
    }
  };

  useEffect(() => {
    const fetchPets = async () => {
      try {
        const response = await axios.get(
          `http://localhost:9090/pets/available?page=${page}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        setPets(response.data["content"]); // Update pets data
        setTotalPages(response.data["totalPages"]); // Store total pages
      } catch (error) {
        console.error("Error fetching pets:", error);
        navigate("/login");
      }
    };

    fetchPets();
  }, [page, navigate, token]);

  // Filter pets based on search input
  const filteredPets = pets.filter((pet) =>
    pet.category.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="h-screen">
      <Navbar />

      {/* Image Tab with Search Bar */}
      <div className="mt-6 w-full flex justify-center">
        <div className="relative w-full mx-auto mt-6">
          <div
            className="relative overflow-hidden transition-all duration-500"
            onMouseEnter={() => setIsHovered(true)}
            onMouseLeave={() => setIsHovered(false)}
          >
            {/* Image with blur effect on hover */}
            <img
              src={dogs}
              alt="Dog"
              className={`w-[80%] mx-auto object-cover transition-all duration-500 ${
                isHovered ? "blur-sm brightness-90" : ""
              }`}
            />

            {/* Search Bar - Visible only when hovered */}
            <div
              className={`absolute inset-0 flex items-center justify-center transition-all duration-500 ${
                isHovered
                  ? "opacity-100 translate-y-0"
                  : "opacity-0 translate-y-5"
              }`}
            >
              <div className="w-[70%] md:w-[50%] bg-white shadow-lg rounded-full flex items-center p-2">
                <input
                  type="text"
                  placeholder="Search for pet category..."
                  className="w-full p-3 rounded-full focus:outline-none text-gray-700"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                />
                <button className="bg-blue-500 text-white px-4 py-2 rounded-full shadow-md hover:bg-blue-600 transition-all">
                  🔍
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Pets List */}
      <div className="flex flex-col items-center justify-center pt-12">
        <h2 className="text-2xl font-bold text-gray-700">
          Pets Available for Sale
        </h2>

        {/* Pets Grid */}
        <div className="mt-6 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-3 gap-6 w-[90%] mx-auto">
          {filteredPets.length > 0 ? (
            filteredPets.map((pet) => (
              <div
                key={pet.id}
                className="bg-white p-4 rounded-lg shadow-lg hover:shadow-xl transition"
              >
                <img
                  src={pet.image}
                  alt={pet.category}
                  className="w-full h-48 object-cover rounded-md"
                />
                <h3 className="mt-2 text-lg font-semibold">{pet.category}</h3>
                <p className="text-gray-600">Breed: {pet.breed}</p>
                <p className="text-green-500 font-bold">Price: ₹{pet.price}</p>
                <button
                  className="mt-3 w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600"
                  onClick={() => navigate(`/buypets/${pet.id}`)}
                >
                  Buy Now
                </button>
              </div>
            ))
          ) : (
            <p className="text-gray-500">No pets found for "{searchTerm}"</p>
          )}
        </div>

        {/* Pagination Controls */}
        <div className="mt-6 flex gap-4">
          <button
            className="bg-gray-500 text-white px-4 py-2 rounded disabled:opacity-50"
            onClick={() => setPage(page - 1)}
            disabled={page === 0} // Disable if on the first page
          >
            ◀ Prev
          </button>
          <span className="text-lg font-semibold">
            Page {page + 1} of {totalPages}
          </span>
          <button
            className="bg-gray-500 text-white px-4 py-2 rounded disabled:opacity-50"
            onClick={() => setPage(page + 1)}
            disabled={page + 1 >= totalPages} // Disable if on the last page
          >
            Next ▶
          </button>
        </div>
        {/* AI Chatbot Section */}
        <div className="mt-10 w-[90%] mx-auto bg-white p-6 rounded-xl shadow-lg">
          <h3 className="text-xl font-bold mb-4 text-gray-800">
            Ask AI for Pet Advice 🐾
          </h3>

          <div className="flex flex-col sm:flex-row gap-4">
            <input
              type="text"
              placeholder="Ask something about pets..."
              className="flex-1 p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400"
              value={chatInput}
              onChange={(e) => setChatInput(e.target.value)}
            />
            <button
              onClick={handleAskAI}
              className="bg-blue-500 text-white px-6 py-2 rounded-md hover:bg-blue-600 transition-all"
            >
              Ask AI
            </button>
          </div>

          {/* Response */}
          {chatResponse && (
            <div className="mt-4 p-4 bg-gray-100 rounded-md text-gray-800 whitespace-pre-line">
              <strong>Answer:</strong> {chatResponse}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Main;
