// Payment.js
import React, { useState } from "react";
import CheckoutForm from "./CheckoutForm";
import axios from "axios";

function PaymentForm() {
  const BANKING_PAYMENT_SERVICE_BASE_URL = process.env.REACT_APP_BANKING_PAYMENT_SERVICE_BASE_URL;
  const [clientSecret, setClientSecret] = useState("");

  const createPaymentIntent = async () => {
    axios
      .post(
        BANKING_PAYMENT_SERVICE_BASE_URL + "/payment/doPayment",
        { amount: 1000, currency: "usd" },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      )
      .then(
        (doPaymentResponse) => {
          setClientSecret(doPaymentResponse.data.client_secret);
        },
        () => {}
      );
  };

  return (
    <div>
      <h1>Complete Your Payment</h1>
      <button onClick={createPaymentIntent}>Create Payment Intent</button>
      {clientSecret && <CheckoutForm clientSecret={clientSecret} />}
    </div>
  );
}

export default PaymentForm;
