import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";

const ProfilePage = () => {
  const [profile, setProfile] = useState(null);
  const [errorMsg, setErrorMsg] = useState("");
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  let userId = null;
  if (token) {
    try {
      const payload = JSON.parse(atob(token.split(".")[1]));
      console.log(payload);
      userId = payload.id; // depends on backend
      console.log("User ID:", userId);
    } catch (e) {
      console.error("Invalid token:", e);
    }
  }

  useEffect(() => {
    const fetchProfile = async () => {
      if (!userId) return;

      try {
        console.log(token);
        const response = await axios.get(
          `http://localhost:9090/users/profiles/${userId}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        setProfile(response.data);
        setErrorMsg("");
      } catch (error) {
        console.error("Error fetching profile:", error);
        const msg =
          error.response?.data?.message ||
          "Something went wrong while fetching the profile.";
        setErrorMsg(msg);
      }
    };

    fetchProfile();
  }, [token]);

  if (profile === null) {
    return (
      <div className="min-h-screen bg-gray-100">
        <Navbar />
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
          {errorMsg && (
            <div className="text-red-600 mb-4 font-medium">{errorMsg}</div>
          )}
          <h2 className="text-xl font-semibold mb-4 text-gray-700">
            Your profile is incomplete
          </h2>
          <button
            onClick={() => navigate("/complete-profile")}
            className="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition"
          >
            Complete Profile
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />
      <div className="max-w-md mx-auto mt-20 bg-white p-6 rounded shadow-md">
        <h2 className="text-2xl font-semibold text-gray-800 mb-6 text-center">
          My Profile
        </h2>
        <div className="space-y-4 text-gray-700 text-lg">
          <p>
            <strong>Username:</strong> {profile.username}
          </p>
          <p>
            <strong>Mobile:</strong> {profile.mobile}
          </p>
          <p>
            <strong>Email:</strong> {profile.email}
          </p>
          <p>
            <strong>Age:</strong> {profile.age}
          </p>
        </div>
      </div>
    </div>
  );
};

export default ProfilePage;
