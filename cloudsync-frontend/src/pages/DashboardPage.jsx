import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
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

              {
                files.map((item) => (

                  <div
                    key={item.id}
                    className="border p-4 rounded-lg bg-gray-50"
                  >

                    <p className="font-semibold">
                      {item.fileName}
                    </p>

                    <p className="text-sm text-gray-500">
                      {item.contentType}
                    </p>

                    <a
                      href={item.fileUrl}
                      target="_blank"
                      rel="noreferrer"
                      className="text-blue-600"
                    >
                      Open File
                    </a>

                  </div>
                ))
              }

            </div>

          </div>

        </div>

      </div>

    </div>
  );
}

export default DashboardPage;