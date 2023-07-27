import axios from "axios";
const axiosRequest = (request, success = null, failure = null) => {
  let token = sessionStorage.getItem("AuthToken");
  token = token ? `Bearer ${token}` : null;

  if (token == null) {
    window.location.href = "/login";
    return;
  }

  let requestData = {
    url: request?.url ?? "",
    baseURL: "http://localhost:8080/",
    method: request.method,
    params: { ...request.params },
    data: { ...request.data },
    headers: {
      Authorization: token,
    },
  };

  axios({ ...requestData })
    .then((response) => {
      if (success) {
        success(response);
      }
    })
    .catch((error) => {
      if (error?.response?.status === 401) {
        window.location.href = "/login";
      }
      if (failure) {
        failure(error);
      }
    });
};

export { axiosRequest };
