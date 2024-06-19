import { Table } from "antd";
import axios from "axios";
import { useEffect, useState } from "react";

const PersonalBankingAllTransactions = () => {
  const PAYMENT_SERVICE_BASE_URL = process.env.REACT_APP_BANKING_PAYMENT_SERVICE_BASE_URL;

  const [allTransactions, setAllTransactions] = useState([]);
  const [expandedRowKeys, setExpandedRowKeys] = useState([] as any[]);

  const handleExpandRows = (expanded: boolean, record: any) => {
    if (expanded) {
      setExpandedRowKeys([record.key, ...expandedRowKeys]);
    } else {
      setExpandedRowKeys(expandedRowKeys.filter((key) => key != record.key));
    }
  };

  useEffect(() => {
    axios.get(PAYMENT_SERVICE_BASE_URL + "/payment/all").then((allPaymentsResponse) =>
      setAllTransactions(
        allPaymentsResponse.data.map((transaction: any, index: number) => ({
          ...transaction,
          key: index,
        }))
      )
    );
  }, []);

  const paymentGridColumns = [
    {
      title: "Transaction Date",
      dataIndex: "paymentStartedAt",
      key: "paymentStartedAt",
      render: (value: string) => value?.replace("T", " ").split("+")[0],
      width: 210,
    },
    {
      title: "User",
      dataIndex: "paymentRequest",
      key: "username",
      render: (value: any) => value?.username,
      width: 210,
    },
    {
      title: "Amount",
      dataIndex: "paymentRequest",
      key: "amount",
      render: (value: any) => value?.amount,
      width: 210,
    },
    {
      title: "Status",
      dataIndex: "status",
      key: "status",
      width: 210,
    },
  ];

  return (
    <div>
      <Table
        dataSource={allTransactions}
        columns={paymentGridColumns}
        pagination={{ pageSize: 20 }}
        expandable={{
          expandedRowKeys: expandedRowKeys,
          onExpand: handleExpandRows,
          expandedRowRender: (transaction: any) => <div style={{ marginLeft: 80 }}></div>,
          rowExpandable: (record: any) => record !== "Not Expandable",
        }}
      />
    </div>
  );
};

export default PersonalBankingAllTransactions;
