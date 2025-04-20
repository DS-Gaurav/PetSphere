import { useNavigate } from "react-router-dom";
import dogs from "../assets/dogs.jpg";
import Navbar from "../components/Navbar";

const Home = () => {
  const navigate = useNavigate();

  return (
    <div className="w-full">
      <Navbar/>
      {/* Hero Section */}
      <div className="relative h-[90vh] w-full">
        {/* Background Image */}
        <div
          className="absolute inset-0 bg-cover bg-center"
          style={{ backgroundImage: `url(${dogs})` }}
        >
          <div className="absolute inset-0 bg-black opacity-40" />
        </div>

        {/* Content on Image */}
        <div className="relative z-10 flex flex-col items-center justify-center h-full px-4">
          <h1 className="text-white text-4xl md:text-5xl font-bold text-center mb-2 drop-shadow-lg">
            Book Pet Care Service in Jaipur
          </h1>
          <p className="text-white text-lg mb-6 text-center drop-shadow-md">
            Your Trusted Pet Companion in Jaipur
          </p>

          {/* Services */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 max-w-5xl w-full">
            <div className="bg-white p-5 rounded-lg shadow-lg text-center hover:scale-105 transition">
              <h3 className="text-orange-600 font-bold text-lg mb-2">
                Pet Grooming in Jaipur
              </h3>
              <p className="text-gray-700">
                Book In-Home Cat and Dog Grooming Service
              </p>
            </div>

            <div className="bg-white p-5 rounded-lg shadow-lg text-center hover:scale-105 transition">
              <h3 className="text-blue-600 font-bold text-lg mb-2">
                Pet Boarding in Jaipur
              </h3>
              <p className="text-gray-700">
                Book Cat and Dog Boarding Service
              </p>
            </div>

            <div className="bg-white p-5 rounded-lg shadow-lg text-center hover:scale-105 transition">
              <h3 className="text-yellow-600 font-bold text-lg mb-2">
                Dog Walking in Jaipur
              </h3>
              <p className="text-gray-700">
                Book Personalised Dog Walkers Near You
              </p>
            </div>

            <div className="bg-white p-5 rounded-lg shadow-lg text-center hover:scale-105 transition">
              <h3 className="text-orange-600 font-bold text-lg mb-2">
                Dog Training in Jaipur
              </h3>
              <p className="text-gray-700">
                Book Dog Training Service At Home
              </p>
            </div>

            <div className="bg-white p-5 rounded-lg shadow-lg text-center hover:scale-105 transition">
              <h3 className="text-indigo-600 font-bold text-lg mb-2">
                Vet on Call in Jaipur
              </h3>
              <p className="text-gray-700">
                Expert Veterinary Service At Your Home and Online
              </p>
            </div>

            <div className="bg-white p-5 rounded-lg shadow-lg text-center hover:scale-105 transition">
              <h3 className="text-red-600 font-bold text-lg mb-2">
                Adopt a Pet in Jaipur
              </h3>
              <p className="text-gray-700">
                Adopt, Don't Shop: Save a Life Today
              </p>
            </div>
          </div>

          {/* Buttons */}
          {/* <div className="mt-8 flex gap-4">
            <button
              className="px-6 py-2 bg-blue-500 text-white rounded-lg shadow hover:bg-blue-600"
              onClick={() => navigate("/login")}
            >
              Login
            </button>
            <button
              className="px-6 py-2 bg-green-500 text-white rounded-lg shadow hover:bg-green-600"
              onClick={() => navigate("/register")}
            >
              Register
            </button>
          </div> */}
        </div>
      </div>
    </div>
  );
};

export default Home;
