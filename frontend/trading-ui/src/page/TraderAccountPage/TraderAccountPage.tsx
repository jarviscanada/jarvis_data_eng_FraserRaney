import './TraderAccountPage.scss'
import {useParams} from "react-router-dom";
import axios from "axios";
import {depositFundsUrl, traderAccountUrl, withdrawFundsUrl} from "../../utils/contants.ts";
import type {TraderAccountView} from "../Dashboard/Dashboard.tsx";
import {useEffect, useState} from "react";
import {Button, Form, Input, Modal} from "antd";
import NavBar from "../../component/NavBar/NavBar.tsx";


function TraderAccountPage() {

  const routeParams = useParams()
  const [traderAccountView, setTraderAccountView] = useState<TraderAccountView>();
  const [isDepositModalVisible, setIsDepositModalVisible] = useState(false);
  const [isWithdrawlModalVisible, setIsWithdrawlModalVisible] = useState(false);
  const [depositForm] = Form.useForm();
  const [withdrawlForm] = Form.useForm();

  const fetchTraderAccountView = async (
      signal?: AbortSignal
  ): Promise<TraderAccountView> => {
    if (routeParams.traderId) {
      const response = await axios.get<TraderAccountView>(
          traderAccountUrl(routeParams.traderId),
          {signal}
      );

      return response.data;
    }
    throw new Error("Missing traderId")
  };

  const showDepositModal = () => setIsDepositModalVisible(true);
  const showWithdrawlModal = () => setIsWithdrawlModalVisible(true);
  const handleDepositCancel = () => {
    setIsDepositModalVisible(false);
    depositForm.resetFields();
  }

  const handleWithdrawCancel = () => {
    setIsWithdrawlModalVisible(false);
    withdrawlForm.resetFields();
  }

  const handleDepositOk = async () => {
    if (routeParams.traderId) {
      try {
        const values = await depositForm.validateFields();
        try {
          await axios.put(depositFundsUrl(routeParams.traderId, values.depositFunds.toString()));
          try {
            const tav = await fetchTraderAccountView();
            setTraderAccountView(tav);
            setIsDepositModalVisible(false);
          } catch (err) {
            console.error("depositFunds.refreshTraderAccountView", err);
          }
        } catch (err) {
          console.error("depositFunds", err);
        }
        setIsDepositModalVisible(false);
        depositForm.resetFields();
      } catch (err) {
        console.error("depositFunds.processForm", err);
      }
    }
  }

  const handleWithdrawOk = async () => {
    if (routeParams.traderId) {
      try {
        const values = await withdrawlForm.validateFields()
        try {
          await axios.put(withdrawFundsUrl(routeParams.traderId, values.withdrawFunds.toString()));
          try {
            const tav = await fetchTraderAccountView();
            setTraderAccountView(tav);
            setIsWithdrawlModalVisible(false);
          } catch (err) {
            console.error("withdrawFunds.refreshTraderAccountView", err);
          }
        } catch (err) {
          console.error("withdrawFunds", err);
        }
        setIsWithdrawlModalVisible(false);
        withdrawlForm.resetFields();
      } catch (err) {
        console.error("withdrawFunds.processForm", err);
      }
    }
  }

  useEffect(() => {
    const controller = new AbortController();

    const load = async () => {
      try {
        const tav = await fetchTraderAccountView(controller.signal);
        setTraderAccountView(tav);
      } catch (err) {
        if (axios.isCancel(err)) return;
        console.error("loadTraderAccountViewData", err);
      }
    };

    load();

    return () => controller.abort();
  }, []);

  return (
      <div className="trader-account-page">
        <NavBar/>
        <div className="trader-account-page-content">
          <div className="title">
            Trader Account
          </div>
          <div className="trader-cards">
            <div className="trader-card">
              <div className="info-row">
                <div className="field">
                  <div className="content-heading">
                    First Name
                  </div>
                  <div className="content">
                    {traderAccountView && traderAccountView.trader.firstName}
                  </div>
                </div>
                <div className="field">
                  <div className="content-heading">
                    Last Name
                  </div>
                  <div className="content">
                    {traderAccountView && traderAccountView.trader.lastName}
                  </div>
                </div>
              </div>
              <div className="info-row">
                <div className="field">
                  <div className="content-heading">
                    Email
                  </div>
                  <div className="content">
                    {traderAccountView && traderAccountView.trader.email}
                  </div>
                </div>
              </div>
              <div className="info-row">
                <div className="field">
                  <div className="content-heading">
                    Date of Birth
                  </div>
                  <div className="content">
                    {traderAccountView && traderAccountView.trader.dateOfBirth}
                  </div>
                </div>
                <div className="field">
                  <div className="content-heading">
                    Country
                  </div>
                  <div className="content">
                    {traderAccountView && traderAccountView.trader.country}
                  </div>
                </div>
              </div>
            </div>
            <div className="trader-card">
              <div className="info-row">
                <div className="field">
                  <div className="content-heading amount">
                    Amount
                  </div>
                  <div className="content amount">
                    ${traderAccountView && traderAccountView.account.amount.toFixed(2)}
                  </div>
                </div>
              </div>
            </div>
            <div className="actions">
              <Button onClick={showDepositModal}>Deposit Funds</Button>
              <Modal title="Deposit Funds" okText="Submit" open={isDepositModalVisible}
                     onOk={handleDepositOk} onCancel={handleDepositCancel}>
                <Form form={depositForm} layout="vertical">
                  <Form.Item label="Funds" name="depositFunds" rules={[{
                    required: true, message: "Please enter an amount"
                  },
                    {
                      pattern: new RegExp(/^[0-9]+$/),
                      message: "Amount has to be a whole number greater than 0."
                    },]}>
                    <Input placeholder="Funds"/>
                  </Form.Item>
                </Form>
              </Modal>
              <Button onClick={showWithdrawlModal}>Withdraw Funds</Button>
              <Modal title="Withdraw Funds" okText="Submit" open={isWithdrawlModalVisible}
                     onOk={handleWithdrawOk} onCancel={handleWithdrawCancel}>
                <Form form={withdrawlForm} layout="vertical">
                  <Form.Item label="Funds" name="withdrawFunds" rules={[{
                    required: true, message: "Please enter an amount"
                  },
                    {
                      pattern: new RegExp(/^[0-9]+$/),
                      message: "Amount has to be a whole number greater than 0."
                    },]}>
                    <Input placeholder="Funds"/>
                  </Form.Item>
                </Form>
              </Modal>
            </div>
          </div>
        </div>
      </div>
  );
}

export default TraderAccountPage;