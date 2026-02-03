//import React from 'react'
import './Dashboard.scss'
import NavBar from "../../component/NavBar/NavBar.tsx";
import type {dataEntry} from "../../component/TraderList/TraderList.tsx";
import TraderList from "../../component/TraderList/TraderList.tsx";
import TraderListData from '../../component/TraderList/TraderListData.json'
import {Button, DatePicker, Form, Input, Modal} from 'antd'
import {useEffect, useState} from 'react'
import axios from "axios";
import {tradersUrl} from "../../utils/contants.ts";


function Dashboard() {
  // Initializing State

  const [isModalVisible, setIsModalVisible] = useState(false);
  const [traders, setTraders] = useState<dataEntry[]>(TraderListData);

  const [form] = Form.useForm();

  const getTraders = async () => {
    const response = await axios.get(tradersUrl);
    if (response) {
      console.log(response.data);
    }
  }

  useEffect(() => {
    getTraders()
  }, [])

  const showModal = () => setIsModalVisible(true);
  const handleCancel = () => {
    form.resetFields();
    setIsModalVisible(false);
  };
  const handleOk = async () => {
    try {
      const values = await form.validateFields();
      console.log("New trader:", values);
      // Add to traders state if mocking
      setTraders([...traders, {...values, id: Date.now(), key: Date.now().toString()}]);
      form.resetFields();
      setIsModalVisible(false);
    } catch (err) {
      console.error(err);
    }
  };
  /*
  useEffect(() => {
    getTraders()
  }, [])
  */
  const onTraderDelete = async (id: number) => {
    console.log("Trader " + id + " is deleted.")
    // axios.delete("")
    //await getTraders()
  }


  return (
      <div className="dashboard">
        {/* Include NavBar below */}
        <NavBar/>
        <div className="dashboard-content">
          <div className="title">
            Dashboard
            <div className="add-trader-button">
              <Button onClick={showModal}>Add New Trader</Button>
              <Modal
                  title="Add New Trader"
                  okText="Submit"
                  open={isModalVisible} // ? In AntD v5+, use `open` instead of `visible`
                  onOk={handleOk}
                  onCancel={handleCancel}
              >
                <Form form={form} layout="vertical">
                  <Form.Item label="First Name" name="firstName" rules={[{required: true}]}>
                    <Input placeholder="John"/>
                  </Form.Item>
                  <Form.Item label="Last Name" name="lastName" rules={[{required: true}]}>
                    <Input placeholder="Doe"/>
                  </Form.Item>
                  <Form.Item label="Email" name="email" rules={[{type: "email", required: true}]}>
                    <Input/>
                  </Form.Item>
                  <Form.Item label="Country" name="country">
                    <Input/>
                  </Form.Item>
                  <Form.Item label="Date of Birth" name="dob" rules={[{required: true}]}>
                    <DatePicker style={{width: "100%"}}/>
                  </Form.Item>
                </Form>
              </Modal>
            </div>
          </div>
          <TraderList onTraderDeleteClick={onTraderDelete}/>
        </div>
      </div>
  )
}

export default Dashboard