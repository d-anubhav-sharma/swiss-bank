import { render, screen } from "@testing-library/react";
import Home from "../../home/Home";

describe("Home component", () => {
  test("renders with initial state", () => {
    render(<Home />);
    expect(screen.getByText("Welcome to Swiss Bank")).toBeInTheDocument();
    expect(
      screen.getByText("We are committed to providing our clients with the highest level of service and security.")
    ).toBeInTheDocument();
    const homeScreenImages = screen.getAllByAltText("Swiss Bank Home Screen Image");
    expect(homeScreenImages.length).toBeGreaterThanOrEqual(3);
  });
});
