import axios from "axios";

const checkLoginStatus = async () => {
  const BANKING_USER_SERVICE_BASE_URL =
    process.env.REACT_APP_BANKING_USER_SERVICE_BASE_URL;
  try {
    const response = await axios.get(
      BANKING_USER_SERVICE_BASE_URL + "/test/get"
    );
    return response.headers["username"];
  } catch (error) {}
};
export default checkLoginStatus;
