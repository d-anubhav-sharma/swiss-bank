import axios from "axios";

const createPaymentIntent = (
  paymentIntentRequest: any,
  onPaymentIntentCreated: Function,
  onPaymentIntentFailed: Function
) => {
  axios
    .post("http://localhost:10009/payment-service/payment/createPaymentIntent", paymentIntentRequest, {
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
