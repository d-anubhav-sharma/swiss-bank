import { Dialog, DialogContent, DialogTitle } from "@mui/material";
import { Button } from "antd";
import { useSelector } from "react-redux";

const ConfirmationDialogBox = () => {
  const { confirmationDialogBoxState } = useSelector((state: any) => state.reducer);
  return (
    <Dialog
      onClose={confirmationDialogBoxState.handleClose}
      open={confirmationDialogBoxState.open}
      className="confirmation-dialog-box"
      style={{ padding: 100 }}
    >
      <DialogTitle>{confirmationDialogBoxState.title}</DialogTitle>
      <DialogContent> {confirmationDialogBoxState.message}</DialogContent>
      <div style={{ display: "flex" }}>
        <Button style={{ flexGrow: 1 }} type="primary" onClick={confirmationDialogBoxState.actionConfirm}>
          Confirm
        </Button>
        <Button style={{ flexGrow: 1 }} type="default" onClick={confirmationDialogBoxState.actionReject}>
          Cancel
        </Button>
      </div>
    </Dialog>
  );
};

export default ConfirmationDialogBox;
