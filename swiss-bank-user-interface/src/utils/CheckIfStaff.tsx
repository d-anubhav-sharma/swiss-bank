import axios from "axios";
import checkLoginStatus from "./CheckLoggedIn";

const checkIfStaff = async () => {
  const BANKING_USER_SERVICE_BASE_URL =
    process.env.REACT_APP_BANKING_USER_SERVICE_BASE_URL;
  try {
    const loggedIn = await checkLoginStatus();
    if (!loggedIn) return false;
    const response = await axios.get(
      BANKING_USER_SERVICE_BASE_URL + "/admin/admin-test/get"
    );
    return response.data;
  } catch (error) {
    console.error("Doesn't seem to be an admin");
    return false;
  }
};
export default checkIfStaff;
