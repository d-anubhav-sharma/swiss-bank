import { Button, FormControl, MenuItem, Select, TextField } from "@mui/material";
import { useDispatch, useSelector } from "react-redux";
import { setActiveContentPage, setUserMessageBoxState } from "../../store/slice";
import { useState } from "react";
import CheckoutForm from "../payment/CheckoutForm";
import { createPaymentIntent } from "../../utils/PaymentUtils";
import "./PersonalBankingCreateAccount.css";

const PersonalBankingCreateAccount = () => {
  const dispatch = useDispatch();
  const [initialAmount, setInitialAmount] = useState(5000);
  const [paymentCheckoutFormVisible, setPaymentCheckoutFormVisible] = useState(false);
  const [clientSecret, setClientSecret] = useState("");
  const { loggedInUser } = useSelector((state: any) => state.reducer);

  const handleGotoTab = (tabName: string) => {
    dispatch(setActiveContentPage(tabName));
  };

  const handlePaymentFailure = (error: any) => {
    console.log(error);
    dispatch(
      setUserMessageBoxState({
        message: "Payment failed with error: " + error.message,
        level: "error",
        visible: "true",
      })
    );
    setClientSecret("");
    setPaymentCheckoutFormVisible(false);
  };

  const handlePaymentSuccess = (paymentSuccessResponse: any) => {
    console.log(paymentSuccessResponse);
    dispatch(
      setUserMessageBoxState({
        message: "Payment success with id: " + paymentSuccessResponse?.paymentIntent?.id,
        level: "success",
        visible: "true",
      })
    );
    setClientSecret("");
    setPaymentCheckoutFormVisible(false);
  };

  const payAndCreateAccount = () => {
    createPaymentIntent(
      { amount: initialAmount, currency: "USD", username: loggedInUser },
      (responseData: any) => {
        setClientSecret(responseData.data.client_secret);
        setPaymentCheckoutFormVisible(true);
      },
      (error: any) => {
        dispatch(setUserMessageBoxState({ message: error.message, level: "error", visible: true }));
      }
    );
  };

  const noButtonStyle = {
    border: "none",
    background: "transparent",
    color: "blue",
    padding: 0,
    margin: 0,
    fontSize: 12,
  };
  return (
    <div>
      <h3>Create Account</h3>
      <div>
        Just pay opening account balance and submit. Its that simple to open an account. Please ensure below points to
        enjoy maximum benfits
        <ol style={{ color: "red", fontSize: 12 }}>
          <li>First account must always be Savings account</li>
          <li>If any of your KYC submissions are pending or rejected, you won't be able to create an account</li>
          <li>
            Maintain atleast &#8377; 5000 in your account. Failing to maintain this might lead to account being frozen.
            Note: we do not deduct any charges/fines against low balance
          </li>
          <li>
            Keep your address and other KYC related details updated at all times. This is required for regulatory
            purposes. Any inconsistency or change in these details should be brought to our notice immediately to avoid
            any legal complications.
          </li>
          <li>
            In case of account being frozen or disabled or blocked, all activities against all accounts of the user will
            be restricted to read only. In such scenario, reach out to your local branch or via{" "}
            <button style={noButtonStyle} onClick={() => handleGotoTab("contact")}>
              Contacts
            </button>
          </li>
        </ol>
      </div>
      <hr></hr>
      <div style={{ width: 300, marginTop: 30 }}>
        Account Type:{" "}
        <FormControl fullWidth>
          <Select defaultValue={"SAVING"}>
            <MenuItem value="SAVING">Savings</MenuItem>
          </Select>
        </FormControl>
        <div style={{ marginTop: 20 }}>
          Initial Amount:{" "}
          <TextField
            type="number"
            fullWidth
            value={initialAmount}
            InputProps={{ startAdornment: <span>&#8377;</span> }}
            onChange={(event: any) => {
              console.log(setInitialAmount(event.target.value));
            }}
            error={initialAmount < 5000}
          ></TextField>
        </div>
        <div style={{ marginTop: 20 }}>
          <Button type="submit" variant="contained" fullWidth onClick={payAndCreateAccount}>
            Pay & Create Account
          </Button>
        </div>
      </div>
      <div className="create-account-checkout-form">
        {paymentCheckoutFormVisible && (
          <CheckoutForm
            clientSecret={clientSecret}
            onPaymentFailure={handlePaymentFailure}
            onPaymentSuccess={handlePaymentSuccess}
          />
        )}
      </div>
    </div>
  );
};
export default PersonalBankingCreateAccount;
