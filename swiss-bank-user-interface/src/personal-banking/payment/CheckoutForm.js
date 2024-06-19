// CheckoutForm.js
import React, { useState } from "react";
import { CardElement, useStripe, useElements } from "@stripe/react-stripe-js";
import "./CheckoutForm.css";
import { useDispatch, useSelector } from "react-redux";
import { setAllProgressMessages } from "../../store/slice";

const CheckoutForm = ({
  clientSecret,
  onPaymentSuccess = (data) => {
    console.log(data);
  },
  onPaymentFailure = (error) => {
    console.log(error);
  },
}) => {
  const stripe = useStripe();
  const elements = useElements();
  const [error, setError] = useState(null);
  const { allProgressMessages } = useSelector((state) => state.reducer);
  const dispatch = useDispatch();

  const handleSubmit = async (event) => {
    event.preventDefault();
    let id = Math.random() * 1000000;
    dispatch(
      setAllProgressMessages([
        ...allProgressMessages,
        {
          messageId: id,
          message: "Processing your payment",
        },
      ])
    );
    stripe
      .createPaymentMethod({
        type: "card",
        card: elements.getElement(CardElement),
        billing_details: {
          name: "Customer Name",
        },
      })
      .then(
        (responseData) => {
          stripe.confirmCardPayment(clientSecret, { payment_method: responseData.paymentMethod.id }).then(
            (confirmPaymentData) => {
              onPaymentSuccess(confirmPaymentData);
              hideProgressMessageWithId(id);
            },
            (error) => {
              onPaymentFailure(error);
              hideProgressMessageWithId(id);
            }
          );
        },
        (error) => {
          onPaymentFailure(error);
          hideProgressMessageWithId(id);
        }
      );
  };

  const hideProgressMessageWithId = (id) => {
    dispatch(setAllProgressMessages(allProgressMessages.filter((message) => message.messageId !== id)));
  };

  return (
    <div className="checkout-form-container">
      <form className="checkout-form" onSubmit={handleSubmit}>
        <h2>Complete Your Payment</h2>
        <h4>Stripe Payment Gateway</h4>
        <div style={{ fontSize: 12, marginBottom: 50 }}>
          <p style={{ color: "orange" }}>
            Please do not refresh or close the window unless you see a success or failure message. On payment
            success/failure you should receive a transaction id. Please save it for any future reference of this
            transaction
          </p>
          <p>Right now, we support only card payments. We are working on integrating internet banking as well</p>
        </div>
        <div className="card-element">
          <CardElement />
        </div>
        <button className="checkout-button" type="submit" disabled={!stripe}>
          Pay
        </button>
        {error && <div className="error-message">{error}</div>}
      </form>
    </div>
  );
};
CheckoutForm.propTypes = {
  clientSecret: String,
  onPaymentSuccess: Function,
  onPaymentFailure: Function,
};
export default CheckoutForm;
