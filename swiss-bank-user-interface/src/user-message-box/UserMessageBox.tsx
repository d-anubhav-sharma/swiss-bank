import { useSelector, useDispatch } from "react-redux";
import "./UserMessageBox.css";
import { useEffect } from "react";
import { setUserMessageBoxState } from "../store/slice";

const UserMessageBox = () => {
  const dispatch = useDispatch();
  const { message, level, visible } = useSelector(
    (state: any) => state.reducer.userMessageBoxState
  );

  useEffect(() => {
    if (visible) {
      const timer = setTimeout(() => {
        dispatch(setUserMessageBoxState({ visible: false }));
      }, 5000);
      return () => clearTimeout(timer);
    }
  }, [visible, dispatch]);

  const getBackgroundColor = (level: any) => {
    switch (level) {
      case "info":
        return "#d1ecf1";
      case "warn":
        return "#fff3cd";
      case "progress":
        return "#cce5ff";
      case "error":
        return "#f8d7da";
      default:
        return "#ffffff";
    }
  };

  return (
    visible && (
      <div
        className="user-message-box"
        style={{ backgroundColor: getBackgroundColor(level) }}
      >
        {message}
      </div>
    )
  );
};

export default UserMessageBox;
