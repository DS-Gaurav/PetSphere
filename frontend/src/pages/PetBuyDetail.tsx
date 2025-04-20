import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import Navbar from "../components/Navbar";

const PetBuyDetail = () => {
  const { id } = useParams();
  const [pet, setPet] = useState(null);
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  useEffect(() => {
    const fetchPet = async () => {
      try {
        const response = await axios.get(`http://localhost:9090/pets/${id}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setPet(response.data);
      } catch (error) {
        console.error("Error fetching pet details:", error);
        navigate("/login");
      }
    };

    fetchPet();
  }, [id, navigate, token]);

  const loadRazorpayScript = () => {
    return new Promise((resolve) => {
      const script = document.createElement("script");
      script.src = "https://checkout.razorpay.com/v1/checkout.js";
      script.onload = () => resolve(true);
      script.onerror = () => resolve(false);
      document.body.appendChild(script);
    });
  };

  const handlePayment = async () => {
    const scriptLoaded = await loadRazorpayScript();
    if (!scriptLoaded) {
      alert("Failed to load Razorpay SDK. Please check your connection.");
      return;
    }
    try {
      const params = new URLSearchParams();
      params.append("amount", pet.price * 100); // Razorpay requires paisa
      params.append("currency", "INR");

      const orderRes = await axios.post(
        "http://localhost:9090/api/payments/create-order",
        params,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/x-www-form-urlencoded",
          },
        }
      );

      const orderData = orderRes.data; // Razorpay order response is stringified JSON
      const { id: order_id, amount, currency } = orderData;

      const options = {
        key: "rzp_test_f2sIwDuER6xd3p", // Replace with your real Razorpay key
        amount,
        currency,
        name: "Pet Purchase",
        description: `Buying ${pet.breed}`,
        image: pet.image,
        order_id,
        handler: function (response) {
          console.log("Payment Success:", response);
          navigate("/thankyou");
        },
        prefill: {
          name: "Your Name",
          email: "email@example.com",
          contact: "9999999999",
        },
        theme: {
          color: "#3399cc",
        },
      };

      const razor = new window.Razorpay(options);
      razor.open();
    } catch (err) {
      console.error("Error during Razorpay payment:", err);
    }
  };

  if (!pet) {
    return (
      <div className="text-center mt-12 text-lg text-gray-600">
        Loading pet details...
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />
      <div className="max-w-4xl mx-auto mt-12 bg-white p-6 rounded-lg shadow-md">
        <h2 className="text-3xl font-bold text-gray-800 text-center mb-6">
          {pet.category} - {pet.breed}
        </h2>
        <div className="flex flex-col md:flex-row gap-8">
          <img
            src={pet.image}
            alt={pet.category}
            className="w-full md:w-1/2 h-64 object-cover rounded-lg shadow"
          />
          <div className="space-y-4 text-gray-700 text-lg">
            <p><strong>Breed:</strong> {pet.breed}</p>
            <p><strong>Age:</strong> {pet.age} months</p>
            <p><strong>Gender:</strong> {pet.gender}</p>
            <p><strong>Price:</strong> ₹{pet.price}</p>
            <p>
              <strong>Health Report:</strong>{" "}
              <a
                href={pet.mceretificate}
                target="_blank"
                rel="noopener noreferrer"
                className="text-blue-600 underline"
              >
                View Report
              </a>
            </p>
          </div>
        </div>

        <div className="mt-8 flex justify-center">
          <button
            onClick={handlePayment}
            className="bg-blue-600 text-white py-3 px-6 rounded-lg hover:bg-blue-700 text-lg transition"
          >
            Proceed to Checkout 🛒
          </button>
        </div>
      </div>
    </div>
  );
};

export default PetBuyDetail;
