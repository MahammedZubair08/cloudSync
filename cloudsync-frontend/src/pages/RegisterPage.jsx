import { useState } from "react";
import api from "../api/axios";
import { useNavigate } from "react-router-dom";

function RegisterPage() {

  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    name: "",
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
        "/api/auth/register",
        formData
      );

      alert(response.data);

      navigate("/");

    } catch (error) {

      console.error(error);

      alert("Registration failed");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">

      <div className="bg-white p-8 rounded-xl shadow-md w-96">

        <h1 className="text-3xl font-bold mb-6 text-center">
          Register
        </h1>

        <form
          className="space-y-4"
          onSubmit={handleSubmit}
        >

          <input
            type="text"
            name="name"
            placeholder="Name"
            onChange={handleChange}
            className="w-full border p-3 rounded-lg"
          />

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
            className="w-full bg-green-600 text-white p-3 rounded-lg"
          >
            Register
          </button>

        </form>
      </div>
    </div>
  );
}

export default RegisterPage;