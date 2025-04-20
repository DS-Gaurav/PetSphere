import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";

const Navbar = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isProfileMenuOpen, setIsProfileMenuOpen] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  const token = localStorage.getItem("token");
  let username = null;
  if (token) {
    try {
      const payload = JSON.parse(atob(token.split(".")[1])); // Decode base64 payload
      console.log(payload);
      username = payload.sub; // Adjust based on your token's payload structure
    } catch (e) {
      console.error("Invalid token:", e);
    }
  }

  const handleMenuToggle = () => setIsMenuOpen(!isMenuOpen);
  const handleProfileMenuToggle = () =>
    setIsProfileMenuOpen(!isProfileMenuOpen);
  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  // Function to check if a tab is active
  const isActive = (path: string) => location.pathname === path;

  return (
    <nav className="bg-white border-gray-200 dark:bg-gray-900">
      <div className="max-w-screen-xl flex flex-wrap items-center justify-between mx-auto p-4">
        <a href="/main" className="flex items-center space-x-3">
          <img
            src="https://flowbite.com/docs/images/logo.svg"
            className="h-8"
            alt="Logo"
          />
          <span className="self-center text-2xl font-semibold dark:text-white">
            PetSpare
          </span>
        </a>

        <div className="flex md:order-2">
          {/* Menu toggle button */}
          <button
            type="button"
            onClick={handleMenuToggle}
            className="md:hidden text-gray-500 dark:text-gray-400 hover:bg-gray-100 dark:hover:bg-gray-700 focus:outline-none rounded-lg text-sm p-2.5 me-1"
          >
            <svg
              className="w-5 h-5"
              aria-hidden="true"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 20 20"
            >
              <path
                stroke="currentColor"
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d="M1 1h15M1 7h15M1 13h15"
              />
            </svg>
          </button>

          {/* Profile menu */}
          {token ? (
            <button
              onClick={handleProfileMenuToggle}
              className="text-gray-500 dark:text-gray-400 hover:bg-gray-100 dark:hover:bg-gray-700 rounded-lg p-2"
            >
              <span className="text-white bg-blue-500 rounded-full w-8 h-8 flex items-center justify-center">
                {username?.charAt(0).toUpperCase()}
              </span>
            </button>
          ) : (
            <button
              onClick={() => navigate("/login")}
              className="bg-blue-500 hover:bg-blue-600 text-white font-medium px-4 py-2 rounded-lg"
            >
              Login
            </button>
          )}
        </div>

        {/* Menu items */}
        <div
          className={`items-center w-full md:flex md:w-auto md:order-1 ${
            isMenuOpen ? "block" : "hidden"
          }`}
        >
          <ul className="flex flex-col p-4 md:p-0 mt-4 font-medium border rounded-lg bg-gray-50 md:space-x-8 md:flex-row md:mt-0 md:border-0 md:bg-white dark:bg-gray-800 md:dark:bg-gray-900">
            <li>
              <a
                href="/main"
                className={`block py-2 px-3 rounded-sm ${
                  isActive("/main")
                    ? "text-white bg-blue-700"
                    : "text-gray-900 hover:bg-gray-100 md:hover:bg-transparent md:hover:text-blue-700 dark:text-white dark:hover:bg-gray-700"
                }`}
              >
                Home
              </a>
            </li>
            <li>
              <a
                href="/mypets"
                className={`block py-2 px-3 rounded-sm ${
                  isActive("/mypets")
                    ? "text-white bg-blue-700"
                    : "text-gray-900 hover:bg-gray-100 md:hover:bg-transparent md:hover:text-blue-700 dark:text-white dark:hover:bg-gray-700"
                }`}
              >
                My Pets
              </a>
            </li>
            <li>
              <a
                href="/about"
                className={`block py-2 px-3 rounded-sm ${
                  isActive("/about")
                    ? "text-white bg-blue-700"
                    : "text-gray-900 hover:bg-gray-100 md:hover:bg-transparent md:hover:text-blue-700 dark:text-white dark:hover:bg-gray-700"
                }`}
              >
                About
              </a>
            </li>
            <li>
              <a
                href="/services"
                className={`block py-2 px-3 rounded-sm ${
                  isActive("/services")
                    ? "text-white bg-blue-700"
                    : "text-gray-900 hover:bg-gray-100 md:hover:bg-transparent md:hover:text-blue-700 dark:text-white dark:hover:bg-gray-700"
                }`}
              >
                Services
              </a>
            </li>
          </ul>
        </div>

        {/* Profile dropdown menu */}
        {isProfileMenuOpen && token && (
          <div className="absolute right-0 mt-2 w-48 bg-white border rounded-lg shadow-md">
            <ul className="py-2 text-gray-900">
              <li>
                <button
                  onClick={() => navigate("/profile")}
                  className="block px-4 py-2 text-sm"
                >
                  Profile
                </button>
              </li>
              <li>
                <button
                  onClick={handleLogout}
                  className="block px-4 py-2 text-sm text-red-600"
                >
                  Logout
                </button>
              </li>
            </ul>
          </div>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
