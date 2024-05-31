import { useState } from "react";
import { useDispatch } from "react-redux";
import { setActiveContentPage, setUserMessageBoxState } from "../store/slice";
import "./RegisterForm.css";
import axios from "axios";

const RegisterForm = () => {
  const BANKING_USER_SERVICE_BASE_URL =
    process.env.REACT_APP_BANKING_USER_SERVICE_BASE_URL;
  const dispatch = useDispatch();
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [statusColor, setStatusColor] = useState("white");
  const [passwordMatched, setPasswordMatched] = useState(0);

  const passwordMatchedColorMap = [];
  const validateUsername = (currentUsername: string) => {
    if (/^\w{3,50}$/.test(currentUsername))
      axios
        .get(
          BANKING_USER_SERVICE_BASE_URL +
            "/auth/checkUsernameAvailable?username=" +
            currentUsername
        )
        .then(
          () => setStatusColor("green"),
          () => setStatusColor("red")
        );
    else {
      setStatusColor("red");
    }
  };

  const validatePassword = (currentConfirmPassword: string) => {
    if (!password || !currentConfirmPassword) {
      setPasswordMatched(0);
    }
    if (password != currentConfirmPassword) {
      setPasswordMatched(-1);
    } else {
      setPasswordMatched(1);
    }
  };

  const handleSubmit = (event: any) => {
    event.preventDefault();
    if (passwordMatched === 1 && statusColor == "green") {
      axios
        .post(BANKING_USER_SERVICE_BASE_URL + "/auth/register", {
          username: username,
          password: password,
          email: email,
        })
        .then(
          () => {
            dispatch(setActiveContentPage("login"));
            dispatch(
              setUserMessageBoxState({
                message: "register success",
                level: "info",
                visible: true,
              })
            );
          },
          (error) => {
            console.log(error);
            dispatch(
              setUserMessageBoxState({
                message:
                  "user registration failed: " + error?.response?.data?.message,
                level: "error",
                visible: true,
              })
            );
          }
        );
    }
  };

  return (
    <div className="form-container">
      <h2>Register</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="username">Username:</label>
          <input
            type="text"
            id="username"
            value={username}
            onChange={(event: any) => {
              setUsername(event.target.value);
              validateUsername(event.target.value);
            }}
            required
            pattern="[A-Za-z0-9_]{3,50}"
          />
          <span style={{ color: statusColor }} className="username-status-sign">
            {statusColor == "red" ? <span>&#33;</span> : <span>&#10004;</span>}
          </span>
        </div>
        <div className="form-group">
          <label htmlFor="email">Email:</label>
          <input
            type="email"
            id="email"
            required
            value={email}
            onChange={(event: any) => {
              setEmail(event.target.value);
            }}
          />
        </div>
        <div className="form-group">
          <label htmlFor="password">Password:</label>
          <input
            type="password"
            id="password"
            required
            value={password}
            onChange={(event: any) => {
              setPassword(event.target.value);
            }}
          />
        </div>
        <div className="form-group">
          <label htmlFor="confirmPassword">Confirm Password:</label>
          <input
            type="password"
            id="confirmPassword"
            required
            value={confirmPassword}
            onChange={(event: any) => {
              setConfirmPassword(event.target.value);
              validatePassword(event.target.value);
            }}
          />
          <span
            style={{
              color:
                passwordMatched === 1
                  ? "green"
                  : passwordMatched === -1
                  ? "red"
                  : "white",
            }}
            className="password-status-sign"
          >
            {passwordMatched === -1 ? (
              <span>&#33;</span>
            ) : passwordMatched === 1 ? (
              <span>&#10004;</span>
            ) : (
              <span></span>
            )}
          </span>
        </div>
        <button type="submit">Register</button>
      </form>
      <p>
        Already have an account?{" "}
        <button
          onClick={() => {
            dispatch(setActiveContentPage("login"));
          }}
        >
          Login
        </button>
      </p>
    </div>
  );
};

export default RegisterForm;
