import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import dogs from "../assets/dogs.jpg";
import Navbar from "../components/Navbar"; // adjust path if needed

const Register = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errors, setErrors] = useState<{ username?: string; password?: string }>({});
  const navigate = useNavigate();

  const validateForm = () => {
    const newErrors: { username?: string; password?: string } = {};

    if (!username.trim()) {
      newErrors.username = "Username is required";
    }

    if (!password) {
      newErrors.password = "Password is required";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleRegister = async () => {
    if (!validateForm()) return;

    try {
      await axios.post("http://localhost:9090/auth/register", {
        username,
        password,
      });

      navigate("/login");
    } catch (error) {
      if (axios.isAxiosError(error) && error.response?.data?.message) {
        setErrors({ username: error.response.data.message });
      } else {
        console.error("An unexpected error occurred.");
      }
    }
  };

  return (
    <div
      className="min-h-screen w-screen bg-cover bg-center flex flex-col"
      style={{ backgroundImage: `url(${dogs})` }}
    >
      <Navbar />

      <div className="flex-grow flex items-center justify-center px-4 py-10">
        <div className="bg-white bg-opacity-90 p-8 rounded-2xl shadow-2xl w-full max-w-md backdrop-blur-sm">
          <h2 className="text-3xl font-bold mb-6 text-center text-gray-800">Register</h2>

          <input
            type="text"
            placeholder="Username"
            className="w-full p-3 border rounded-lg mb-4 focus:outline-none focus:ring-2 focus:ring-blue-500"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          {errors.username && <p className="text-red-600 text-sm mb-3">{errors.username}</p>}

          <input
            type="password"
            placeholder="Password"
            className="w-full p-3 border rounded-lg mb-4 focus:outline-none focus:ring-2 focus:ring-blue-500"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          {errors.password && <p className="text-red-600 text-sm mb-4">{errors.password}</p>}

          <button
            className="w-full bg-green-600 text-white py-3 rounded-lg font-semibold hover:bg-green-700 transition"
            onClick={handleRegister}
          >
            Register
          </button>

          <p className="text-center mt-6 text-gray-700">
            Already registered?{" "}
            <button
              onClick={() => navigate("/login")}
              className="text-blue-600 font-medium hover:underline"
            >
              Login here
            </button>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Register;
