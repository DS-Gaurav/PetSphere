// src/pages/Services.tsx
import React from 'react';
import Navbar from '../components/Navbar';

const Services = () => {
  const handleNearbyPetClinicsClick = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          const url = `https://www.google.com/maps/search/pet+clinics/@${latitude},${longitude},15z`;
          window.open(url, '_blank');
        },
        (error) => {
          alert('Location access denied or unavailable.');
          console.error(error);
        }
      );
    } else {
      alert('Geolocation is not supported by your browser.');
    }
  };
  const handleNearbyPetStoreClick = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          const url = `https://www.google.com/maps/search/pet+store/@${latitude},${longitude},15z`;
          window.open(url, '_blank');
        },
        (error) => {
          alert('Location access denied or unavailable.');
          console.error(error);
        }
      );
    } else {
      alert('Geolocation is not supported by your browser.');
    }
  };
  const handleNearbyPetNgoClick = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          const url = `https://www.google.com/maps/search/animal+ngo/@${latitude},${longitude},15z`;
          window.open(url, '_blank');
        },
        (error) => {
          alert('Location access denied or unavailable.');
          console.error(error);
        }
      );
    } else {
      alert('Geolocation is not supported by your browser.');
    }
  };

  const services = [
    {
      title: 'Pet Grooming',
      description: 'Get detailed info and recommendations related to pet grooming.',
    },
    {
      title: 'Pet Food Recommendation',
      description: 'Get detailed info and recommendations related to pet food.',
    },
    {
      title: 'Nearby Pet Store',
      description: 'Find pet stores near your location using Google Maps.',
      onClick: handleNearbyPetStoreClick,
    },
    {
      title: 'Nearby Pet Clinics',
      description: 'Find pet clinics near your location using Google Maps.',
      onClick: handleNearbyPetClinicsClick,
    },
    {
      title: 'Nearby NGOs',
      description: 'Find animal NGOs near your location using Google Maps.',
      onClick: handleNearbyPetNgoClick,
    },
  ];

  return (
    <>
      <Navbar />
      <div className="max-w-screen-xl mx-auto p-4 mt-4">
        <h1 className="text-3xl font-bold mb-6 text-center">Our Services</h1>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {services.map((service, idx) => {
            if (service.link) {
              return (
                <a
                  key={idx}
                  href={service.link}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="p-6 bg-white rounded-lg shadow hover:shadow-md transition block"
                >
                  <h2 className="text-xl font-semibold text-blue-700">{service.title}</h2>
                  <p className="text-gray-600 mt-2">{service.description}</p>
                </a>
              );
            } else if (service.onClick) {
              return (
                <button
                  key={idx}
                  onClick={service.onClick}
                  className="w-full text-left p-6 bg-white rounded-lg shadow hover:shadow-md transition"
                >
                  <h2 className="text-xl font-semibold text-blue-700">{service.title}</h2>
                  <p className="text-gray-600 mt-2">{service.description}</p>
                </button>
              );
            } else {
              return (
                <div
                  key={idx}
                  className="p-6 bg-white rounded-lg shadow hover:shadow-md transition"
                >
                  <h2 className="text-xl font-semibold text-blue-700">{service.title}</h2>
                  <p className="text-gray-600 mt-2">{service.description}</p>
                </div>
              );
            }
          })}
        </div>
      </div>
    </>
  );
};

export default Services;
