import { useSelector } from "react-redux";

const ProgressItemsBar = () => {
  const { allProgressMessages } = useSelector((state: any) => state.reducer);
  if (allProgressMessages.length)
    return (
      <div id="progressItemsBar">
        {allProgressMessages.map((messageEntry: any) => (
          <div style={{ display: "flex" }}>
            <div className="loader"></div>
            <div key={messageEntry.messageId}>{messageEntry.message}</div>
          </div>
        ))}
      </div>
    );
  return null;
};
export default ProgressItemsBar;
