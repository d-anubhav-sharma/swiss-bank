import axios from "axios";

const createPaymentIntent = (
  paymentIntentRequest: any,
  onPaymentIntentCreated: Function,
  onPaymentIntentFailed: Function
) => {
  const BANKING_PAYMENT_SERVICE_BASE_URL = process.env.REACT_APP_BANKING_PAYMENT_SERVICE_BASE_URL;
  axios
    .post(BANKING_PAYMENT_SERVICE_BASE_URL + "/payment/createPaymentIntent", paymentIntentRequest, {
      headers: {
        "Content-Type": "application/json",
      },
    })
    .then(
      (doPaymentResponse) => {
        onPaymentIntentCreated(doPaymentResponse);
      },
      (error) => {
        onPaymentIntentFailed(error);
      }
    );
};

export { createPaymentIntent };
