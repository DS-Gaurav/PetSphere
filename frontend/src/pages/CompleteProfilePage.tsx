import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";

const CompleteProfilePage = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  const [formData, setFormData] = useState({
    mobile: "",
    email: "",
    age: "",
  });

  const [error, setError] = useState("");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
  
    const mobileRegex = /^[0-9]{10}$/;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const ageNumber = parseInt(formData.age);
  
    if (!formData.mobile || !formData.email || !formData.age) {
      setError("All fields are required.");
      return;
    }
  
    if (!mobileRegex.test(formData.mobile)) {
      setError("Mobile number must be exactly 10 digits.");
      return;
    }
  
    if (!emailRegex.test(formData.email)) {
      setError("Please enter a valid email address.");
      return;
    }
  
    if (isNaN(ageNumber) || ageNumber < 1 || ageNumber > 120) {
      setError("Age must be a number between 1 and 120.");
      return;
    }
  
    try {
      // 1. Submit profile
      await axios.post(
        "http://localhost:9090/users/profiles",
        { ...formData },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      console.log("profile to set ho gyi");
      // 2. Send mail after profile is created
      await axios.post("http://localhost:9090/mail", {
        to: formData.email,
        subject: "Profile Created Successfully ✅",
        body: `Hello 👋,
  
  Your profile has been successfully created with the following details:
  
  📱 Mobile: ${formData.mobile}
  📧 Email: ${formData.email}
  🎂 Age: ${formData.age}
  
  Thank you for completing your profile!
  
  - The Team`,
      },
      {
        headers: { Authorization: `Bearer ${token}` },
      });
      // 3. Navigate to profile page
      navigate("/profile");
    } catch (err) {
      console.error("Profile creation or mail error:", err);
      setError("Failed to submit profile or send mail. Try again.");
    }
  };
  
  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />
      <div className="flex justify-center items-center min-h-screen bg-gray-100">
        <form
          onSubmit={handleSubmit}
          className="w-full max-w-md bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4"
        >
          <h2 className="text-2xl font-bold mb-6 text-center text-gray-800">
            Complete Your Profile
          </h2>

          {error && <p className="text-red-500 mb-4 text-center">{error}</p>}

          <div className="mb-4">
            <label className="block text-gray-700 text-sm font-bold mb-2">
              Mobile
            </label>
            <input
              type="text"
              name="mobile"
              value={formData.mobile}
              onChange={handleChange}
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              placeholder="1234567890"
            />
          </div>

          <div className="mb-4">
            <label className="block text-gray-700 text-sm font-bold mb-2">
              Email
            </label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              placeholder="you@example.com"
            />
          </div>

          <div className="mb-6">
            <label className="block text-gray-700 text-sm font-bold mb-2">
              Age
            </label>
            <input
              type="number"
              name="age"
              value={formData.age}
              onChange={handleChange}
              min="1"
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              placeholder="25"
            />
          </div>

          <button
            type="submit"
            className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
          >
            Submit
          </button>
        </form>
      </div>
    </div>
  );
};

export default CompleteProfilePage;
