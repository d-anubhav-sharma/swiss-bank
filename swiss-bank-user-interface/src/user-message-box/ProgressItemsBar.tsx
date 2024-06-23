import { useSelector } from "react-redux";

const ProgressItemsBar = () => {
  const { allProgressMessages } = useSelector((state: any) => state.reducer);
  if (allProgressMessages.length)
    return (
      <div id="progressItemsBar">
        {allProgressMessages.map((messageEntry: any) => (
          <div key={messageEntry.key} style={{ display: "flex" }}>
            <img src="rupee_icon_golden.png" alt="loading" className="loading_icon_rupee" />
            <div key={messageEntry.messageId}>{messageEntry.message}</div>
          </div>
        ))}
      </div>
    );
  return null;
};
export default ProgressItemsBar;
