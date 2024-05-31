import "./Services.css";
const services = [
  {
    id: 1,
    title: "Personal Banking",
    description:
      "Offering a suite of products designed to meet the diverse financial needs of individuals. Services include checking and savings accounts, personal loans, mortgages, and credit cards. Our personalized approach ensures each client receives tailored solutions to manage their finances efficiently.",
    service_url: "http://localhost:3000/personal-banking",
  },
  {
    id: 2,
    title: "Business Banking",
    description:
      "Catering to small and medium-sized enterprises with a range of services including business checking accounts, commercial loans, cash management, and merchant services. We provide strategic financial solutions to help businesses grow and thrive.",
    service_url: "http://localhost:3000/business-banking",
  },
  {
    id: 3,
    title: "Wealth Management",
    description:
      "Our wealth management services offer bespoke financial planning and investment strategies for high-net-worth individuals. Services include portfolio management, retirement planning, tax optimization, and estate planning, all aimed at preserving and growing wealth.",
    service_url: "http://localhost:3000/wealth-management",
  },
  {
    id: 4,
    title: "Investment Banking",
    description:
      "Providing expert advisory services for mergers and acquisitions, capital raising, and corporate restructuring. Our investment banking division leverages deep industry knowledge and extensive networks to deliver comprehensive financial solutions to corporations and institutions.",
    service_url: "http://localhost:3000/investment-banking",
  },
  {
    id: 5,
    title: "Retail Banking",
    description:
      "Focused on delivering accessible financial services to individuals and families. Our offerings include everyday banking products, online and mobile banking, personal loans, and insurance products, ensuring convenience and financial security for our clients.",
    service_url: "http://localhost:3000/retail-banking",
  },
  {
    id: 6,
    title: "Corporate Banking",
    description:
      "Delivering a full range of services to large corporations, including working capital management, trade finance, syndicated loans, and treasury services. We partner with our clients to provide customized solutions that support their financial objectives and operational needs.",
    service_url: "http://localhost:3000/corporate-banking",
  },
  {
    id: 7,
    title: "Private Banking",
    description:
      "Tailored banking and financial services for affluent individuals and families. Private banking clients enjoy dedicated relationship managers, bespoke financial solutions, and exclusive products designed to meet their unique financial goals.",
    service_url: "http://localhost:3000/private-banking",
  },
  {
    id: 8,
    title: "Asset Management",
    description:
      "Offering professional management of investment portfolios for individuals, institutions, and corporations. Our asset management services include mutual funds, exchange-traded funds (ETFs), and alternative investments, focusing on achieving optimal returns.",
    service_url: "http://localhost:3000/asset-management",
  },
  {
    id: 9,
    title: "Treasury and Trade Solutions",
    description:
      "Providing comprehensive treasury management services, including liquidity management, foreign exchange, trade finance, and risk management. Our solutions help businesses manage their global cash flows and financial risk effectively.",
    service_url: "http://localhost:3000/treasury-trade-solutions",
  },
  {
    id: 10,
    title: "Digital Banking Services",
    description:
      "Enhancing client experience through innovative digital solutions. Services include online and mobile banking, digital wallets, and real-time transaction monitoring, ensuring seamless and secure financial management from anywhere in the world.",
    service_url: "http://localhost:3000/digital-banking-services",
  },
  {
    id: 11,
    title: "Financial Advisory Services",
    description:
      "Offering expert financial advice and strategic planning services to individuals and businesses. Our advisory services include investment advice, financial planning, risk management, and retirement planning, aimed at achieving long-term financial stability.",
    service_url: "http://localhost:3000/financial-advisory-services",
  },
  {
    id: 12,
    title: "Securities Trading",
    description:
      "Providing access to global financial markets through our securities trading platform. Clients can trade stocks, bonds, options, and other financial instruments with the support of our experienced trading professionals.",
    service_url: "http://localhost:3000/securities-trading",
  },
  {
    id: 13,
    title: "Loan and Credit Services",
    description:
      "Offering a variety of lending solutions to meet personal and business needs. Services include personal loans, auto loans, home equity lines of credit, and commercial lending options, all structured to provide flexible and competitive financing.",
    service_url: "http://localhost:3000/loan-credit-services",
  },
  {
    id: 14,
    title: "Insurance Services",
    description:
      "Comprehensive insurance products designed to protect individuals and businesses from unforeseen risks. Our offerings include life insurance, health insurance, property and casualty insurance, and specialty insurance products.",
    service_url: "http://localhost:3000/insurance-services",
  },
  {
    id: 15,
    title: "Trust and Estate Services",
    description:
      "Providing expert management of trusts and estates to ensure the smooth transfer of wealth across generations. Services include trust administration, estate planning, fiduciary services, and philanthropic advisory, all designed to safeguard client legacies.",
    service_url: "http://localhost:3000/trust-estate-services",
  },
];
const Services = () => {
  return (
    <div className="dashboard">
      <h1>Our Services</h1>
      <div className="service-grid">
        {services.map((service) => (
          <div key={service.id} className="service-card">
            <h2>
              <a style={{ textDecoration: "none" }} href={service.service_url}>
                {service.title}
              </a>
            </h2>
            <p>{service.description}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Services;
