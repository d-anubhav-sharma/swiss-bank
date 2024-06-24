import { Search } from "@mui/icons-material";
import { Button, TextField } from "@mui/material";
import { useEffect, useState } from "react";
import "./ListView.css";

const ListView = ({
  listHeader,
  listItems,
  searchEnabled,
  onSelectionChange,
  allowDuplicates,
  keepOrder,
}: {
  listHeader: any;
  listItems: any[];
  searchEnabled?: boolean;
  onSelectionChange?: any;
  allowDuplicates?: boolean;
  keepOrder?: boolean;
}) => {
  const [filterCriteria, setFilterCriteria] = useState("");
  const [selectedItem, setSelectedItem] = useState({} as any);
  const renderListHeader = () => {
    if (listHeader.title) return <div className={listHeader.className}>{listHeader.title}</div>;
    return listHeader;
  };

  const renderSearchIcon = () => {
    if (!searchEnabled) return null;
    return (
      <TextField size="small" onChange={(event) => setFilterCriteria(event.target.value)} label={<Search></Search>} />
    );
  };

  useEffect(() => {
    onSelectionChange?.(selectedItem);
  }, [selectedItem]);

  const renderListItem = (item: any) => {
    if (item instanceof String) return <div>{item}</div>;
    if (item.render) return <div className={item.className}>{item.render(item)}</div>;
    return (
      <Button
        onClick={() => {
          if (selectedItem == item.title) {
            setSelectedItem("");
          } else {
            setSelectedItem(item.title);
          }
        }}
        style={{ paddingLeft: 20, paddingRight: 20, width: "100%", border: "none" }}
        variant={item.title === selectedItem ? "contained" : "outlined"}
        className={item.className}
      >
        {item.title}
      </Button>
    );
  };

  const validateItemTitle = (item: any) => {
    if (!item) return false;
    if (item && typeof item === "string") return item.includes(filterCriteria);
    if (item.title && typeof item.title === "string") return item.title.includes(filterCriteria);
    return true;
  };

  const compareItems = (item1: any, item2: any): number => {
    if (keepOrder) return 0;
    if (typeof item1 === "string") return item1 > item2 ? 1 : -1;
    if (typeof item1.title === "string") return item1.title > item2.title ? 1 : -1;
    return 0;
  };

  const checkRepeated = (item: any, index: number, array: any[]) => {
    if (allowDuplicates) return true;
    if (typeof item === "string") return array.indexOf(item) === index;
    if (typeof item.title === "string") {
      return array.map((item) => item.title).indexOf(item.title) === index;
    }
    return true;
  };

  const renderListItems = () => {
    if (!listItems?.length) return <div>-- No Elements in the list --</div>;
    return (
      <div>
        {[
          renderSearchIcon(),
          ...listItems
            .filter((item: any) => validateItemTitle(item))
            .filter((item: any, index: number, array: any[]) => checkRepeated(item, index, array))
            .sort((item1, item2) => compareItems(item1, item2))
            .map((item: any) => <div key={item.key}>{renderListItem(item)}</div>),
        ]}
      </div>
    );
  };
  return (
    <div style={{ width: "fit-content", padding: 20, minHeight: "50vh" }} className="list-view-card">
      <h4 style={{ justifyContent: "center", alignItems: "center", textAlign: "center" }}>{renderListHeader()}</h4>
      <div style={{ backgroundColor: "white" }}>{renderListItems()}</div>
    </div>
  );
};

export default ListView;
