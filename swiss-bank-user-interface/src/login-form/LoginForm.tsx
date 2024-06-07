import axios from "axios";
import { useState } from "react";
import { useDispatch } from "react-redux";
import { setActiveContentPage, setUserMessageBoxState } from "../store/slice";

const LoginForm = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const BANKING_USER_SERVICE_BASE_URL = process.env.REACT_APP_BANKING_USER_SERVICE_BASE_URL;

  const dispatch = useDispatch();

  const handleSubmit = (event: any) => {
    event.preventDefault();
    axios
      .post(BANKING_USER_SERVICE_BASE_URL + "/auth/login", {
        username: username,
        password: password,
      })
      .then(
        () => {
          window.location.reload();
        },
        (error) => {
          console.error(error);
          dispatch(
            setUserMessageBoxState({
              message: "Error: Invalid username/password",
              level: "error",
              visible: true,
            })
          );
        }
      );
  };

  return (
    <div className="form-container">
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="username">Username:</label>
          <input
            type="text"
            id="username"
            value={username}
            onChange={(event) => setUsername(event.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="password">Password:</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            required
          />
        </div>
        <button type="submit">Login</button>
      </form>
      <p>
        Don't have an account? <button onClick={() => dispatch(setActiveContentPage("register"))}>Register</button>
      </p>
    </div>
  );
};

export default LoginForm;
