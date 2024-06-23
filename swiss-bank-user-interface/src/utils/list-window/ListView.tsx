import { Button } from "@mui/material";
import { useState } from "react";

const ListView = ({ listHeader, listItems }: { listHeader: any; listItems: any[] }) => {
  const [selectedItem, setSelectedItem] = useState({} as any);
  const renderListHeader = () => {
    if (listHeader.title) return <div className={listHeader.className}>{listHeader.title}</div>;
    return listHeader;
  };

  const renderListItem = (item: any) => {
    if (item instanceof String) return <div>{item}</div>;
    if (item.render) return <div className={item.className}>{item.render(item)}</div>;
    return (
      <Button
        onClick={() => setSelectedItem(item.title)}
        style={{ paddingLeft: 20, paddingRight: 20, width: "100%", border: "none" }}
        variant={item.title === selectedItem ? "contained" : "outlined"}
        className={item.className}
      >
        {item.title}
      </Button>
    );
  };

  const renderListItems = () => {
    if (!listItems?.length) return <div>-- No Elements in the list --</div>;
    return (
      <div>
        {listItems.map((item: any) => (
          <div key={item.key}>{renderListItem(item)}</div>
        ))}
      </div>
    );
  };
  return (
    <div style={{ width: "fit-content", padding: 20 }}>
      <h4 style={{ justifyContent: "center", alignItems: "center", textAlign: "center" }}>{renderListHeader()}</h4>
      <div style={{ backgroundColor: "white" }}>{renderListItems()}</div>
    </div>
  );
};

export default ListView;
