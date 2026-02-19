import './Dashboard.scss'
import NavBar from "../../component/NavBar/NavBar.tsx";
import type {Trader} from "../../component/TraderList/TraderList.tsx";
import TraderList from "../../component/TraderList/TraderList.tsx";
import {Button, DatePicker, Form, Input, Modal} from 'antd'
import {useEffect, useState} from 'react'
import axios from "axios";
import {createTraderUrl, deleteTraderUrl, tradersUrl} from "../../utils/contants.ts";

export type TraderAccountView = {
  trader: Trader;
  account: {
    id: number;
    traderId: number;
    amount: number;
  }
}

function Dashboard() {

  const [isModalVisible, setIsModalVisible] = useState(false);
  const [traders, setTraders] = useState<Trader[]>([]);

  const [form] = Form.useForm();

  const fetchTraders = async (
      signal?: AbortSignal
  ): Promise<Trader[]> => {
    const response = await axios.get<TraderAccountView[]>(
        tradersUrl,
        {signal}
    );

    return response.data.map(tav => tav.trader);
  };


  useEffect(() => {
    const controller = new AbortController();

    const load = async () => {
      try {
        const traders = await fetchTraders(controller.signal);
        setTraders(traders);
      } catch (err) {
        if (axios.isCancel(err)) return;
        console.error("loadTradersData", err);
      }
    };

    load();

    return () => controller.abort();
  }, []);


  const showModal = () => setIsModalVisible(true);
  const handleCancel = () => {
    form.resetFields();
    setIsModalVisible(false);
  };
  const handleOk = async () => {
    try {
      const values = await form.validateFields();
      try {
        await axios.post(createTraderUrl, values, {
          headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json'
          }
        });
        try {
          const traders = await fetchTraders();
          setTraders(traders);
        } catch (err) {
          console.error("createNewTrader.fetchTraders", err);
        }
      } catch (err) {
        console.error("createNewTrader", err);
      }

      form.resetFields();
      setIsModalVisible(false);
    } catch (err) {
      console.error("createNewTrader.processForm", err);
    }
  };

  const onTraderDelete = async (id: number) => {
    console.log("Trader " + id + " is deleted.")
    try {
      await axios.delete(deleteTraderUrl(id.toString()), {
        headers: {
          'accept': '*/*'
        }
      });
      try {
        const traders = await fetchTraders();
        setTraders(traders);
      } catch (err) {
        console.error("deleteTrader.fetchTraders", err);
      }
    } catch (err) {
      console.error("deleteTrader", err);
    }
  }


  return (
      <div className="dashboard">
        <NavBar/>
        <div className="dashboard-content">
          <div className="title">
            Dashboard
            <div className="add-trader-button">
              <Button onClick={showModal}>Add New Trader</Button>
              <Modal
                  title="Add New Trader"
                  okText="Submit"
                  open={isModalVisible}
                  onOk={handleOk}
                  onCancel={handleCancel}
              >
                <Form form={form} layout="vertical">
                  <Form.Item label="First Name" name="firstName"
                             rules={[{required: true, message: 'Please enter a first name'}]}>
                    <Input placeholder="John"/>
                  </Form.Item>
                  <Form.Item label="Last Name" name="lastName"
                             rules={[{required: true, message: "Please enter a last name"}]}>
                    <Input placeholder="Doe"/>
                  </Form.Item>
                  <Form.Item label="Email" name="email" rules={[{
                    type: "email",
                    required: true,
                    message: "Please enter a valid email"
                  }]}>
                    <Input/>
                  </Form.Item>
                  <Form.Item label="Country" name="country"
                             rules={[{required: true, message: "Please enter a country"}]}>
                    <Input/>
                  </Form.Item>
                  <Form.Item label="Date of Birth" name="dateOfBirth"
                             rules={[{required: true, message: "Please select a date pf birth"}]}>
                    <DatePicker style={{width: "100%"}}/>
                  </Form.Item>
                </Form>
              </Modal>
            </div>
          </div>
          <TraderList onTraderDeleteClick={onTraderDelete} traders={traders}/>
        </div>
      </div>
  )
}

export default Dashboard