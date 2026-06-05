import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import api from "../api/axios";

function DashboardPage() {

  const navigate = useNavigate();

  const [file, setFile] = useState(null);

  const [uploading, setUploading] =
    useState(false);

  const [message, setMessage] =
    useState("");

  const [files, setFiles] =
    useState([]);

  const logout = () => {

    localStorage.removeItem("token");

    navigate("/");
  };
  const downloadFile = async (id) => {

    try {

      const token = localStorage.getItem("token");

      const response = await axios.get(
        `http://localhost:8080/api/files/download/${id}`,
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );

      window.open(response.data, "_blank");

    } catch (error) {

      console.error(error);
    }
  };

  const deleteFile = async (id) => {
    try {
      await api.delete(`/api/files/${id}`);
      setMessage("File deleted successfully");
      fetchFiles();
    } catch (error) {
      console.error(error);
      setMessage("Delete failed");
    }
  };

  const fetchFiles = async () => {

    try {

      const response =
        await api.get("/api/files");

      setFiles(response.data);

    } catch (error) {

      console.error(error);
    }
  };

  useEffect(() => {

    fetchFiles();

  }, []);

  const handleFileChange = (e) => {

    setFile(e.target.files[0]);
  };

  const handleUpload = async () => {

    if (!file) {

      alert("Select a file first");

      return;
    }

    try {

      setUploading(true);

      const formData = new FormData();

      formData.append("file", file);

      await api.post(
        "/api/files/upload",
        formData,
        {
          headers: {
            "Content-Type":
              "multipart/form-data",
          },
        }
      );

      setMessage(
        "File uploaded successfully"
      );

      fetchFiles();

    } catch (error) {

      console.error(error);

      setMessage("Upload failed");
    }

    finally {

      setUploading(false);
    }
  };

  return (

    <div className="min-h-screen bg-gray-100">

      <div className="bg-white shadow p-4 flex justify-between">

        <h1 className="text-2xl font-bold">
          CloudSync Dashboard
        </h1>

        <button
          onClick={logout}
          className="bg-red-500 text-white px-4 py-2 rounded"
        >
          Logout
        </button>

      </div>

      <div className="p-10">

        <div className="bg-white p-8 rounded-xl shadow max-w-2xl">

          <h2 className="text-3xl font-semibold mb-6">
            Upload File
          </h2>

          <input
            type="file"
            onChange={handleFileChange}
            className="mb-4"
          />

          <button
            onClick={handleUpload}
            disabled={uploading}
            className="bg-blue-600 text-white px-6 py-3 rounded-lg"
          >

            {
              uploading
                ? "Uploading..."
                : "Upload File"
            }

          </button>

          {
            message && (
              <p className="mt-4 text-green-600">
                {message}
              </p>
            )
          }

          <div className="mt-8">

            <h3 className="text-2xl font-semibold mb-4">
              Uploaded Files
            </h3>

            <div className="space-y-3">

              {files?.map((file) => (

                file && (

                  <div
                    key={file.id}
                    className="bg-white p-4 rounded shadow"
                  >

                    <h3 className="font-bold">
                      {file.fileName}
                    </h3>

                    <div className="flex gap-2 mt-2">
                      <button
                        onClick={() => downloadFile(file.id)}
                        className="bg-green-500 text-white px-4 py-2 rounded"
                      >
                        Open File
                      </button>

                      <button
                        onClick={() => deleteFile(file.id)}
                        className="bg-red-500 text-white px-4 py-2 rounded"
                      >
                        Delete
                      </button>
                    </div>

                  </div>

                )

              ))}

            </div>

          </div>

        </div>

      </div>

    </div>
  );
}

export default DashboardPage;