import dogs from "../assets/dogs.jpg";
import Navbar from "../components/Navbar";

const About = () => {
  return (
    <div
      className="min-h-screen w-screen bg-cover bg-center flex flex-col"
      style={{ backgroundImage: `url(${dogs})` }}
    >
      <Navbar />

      <div className="flex-grow flex items-center justify-center px-4 py-12 backdrop-blur-md bg-black/30">
        <div className="bg-white bg-opacity-90 p-8 rounded-xl shadow-2xl max-w-2xl text-center">
          <h1 className="text-4xl font-bold text-gray-800 mb-4">About PetSpare</h1>
          <p className="text-gray-700 text-lg mb-4">
            PetSpare is a platform built with love for pet lovers. Whether you’re here to
            manage your pet profiles, explore services, or just connect with other pet parents, 
            PetSpare has everything covered.
          </p>
          <p className="text-gray-700 text-lg mb-4">
            Our mission is to simplify pet care and bring smiles to both pets and their humans.
            We believe pets are family and they deserve the best.
          </p>
          <p className="text-gray-700 text-lg">
            Feel free to explore our services and let us know how we can help your furry friend!
          </p>
        </div>
      </div>
    </div>
  );
};

export default About;
