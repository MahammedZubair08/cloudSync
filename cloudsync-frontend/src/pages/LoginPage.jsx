import { useState } from "react";
import api from "../api/axios";
import { useNavigate } from "react-router-dom";

function LoginPage() {

  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const handleChange = (e) => {

    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {

    e.preventDefault();

    try {

      const response = await api.post(
        "/api/auth/login",
        formData
      );

      localStorage.setItem(
        "token",
        response.data
      );

      alert("Login successful");

      navigate("/dashboard");

    } catch (error) {

      console.error(error);

      alert("Login failed");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">

      <div className="bg-white p-8 rounded-xl shadow-md w-96">

        <h1 className="text-3xl font-bold mb-6 text-center">
          Login
        </h1>

        <form
          className="space-y-4"
          onSubmit={handleSubmit}
        >

          <input
            type="email"
            name="email"
            placeholder="Email"
            onChange={handleChange}
            className="w-full border p-3 rounded-lg"
          />

          <input
            type="password"
            name="password"
            placeholder="Password"
            onChange={handleChange}
            className="w-full border p-3 rounded-lg"
          />

          <button
            className="w-full bg-blue-600 text-white p-3 rounded-lg"
          >
            Login
          </button>

        </form>
      </div>
    </div>
  );
}

export default LoginPage;