//import React from 'react';
import { Table } from 'antd';
//import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import './TraderList.scss'
import { useState, useEffect } from 'react'
import TraderListData from './TraderListData.json'
import type { ColumnsType } from 'antd/es/table';

type dataEntry = {
  key: string;
  id: number;
  firstName: string;
  lastName: string;
  dob: string;
  country: string;
  email: string;
  amount: number;
  actions: string;
}

const columns: ColumnsType<dataEntry> = [
  {
    title: 'First Name',
    dataIndex: 'firstName',
    key: 'firstName',
  },
  {
    title: 'Last Name',
    dataIndex: 'lastName',
    key: 'lastName',
  },
  {
    title: 'Email',
    dataIndex: 'email',
    key: 'email',
  },
  {
    title: 'Date of Birth',
    dataIndex: 'dob',
    key: 'dob',
  },
  {
    title: 'Country',
    dataIndex: 'country',
    key: 'country',
  },
  {
    title: 'Actions',
    dataIndex: 'actions',
    key: 'actions'
  },
];


function TraderList(props: unknown[]) {
  console.log(props)
  // Initialization of columns for table

  //const [tableColumns, setTableColumns] = useState(columns)
  const [dataSource, setDataSource] = useState<dataEntry[]>([])



  useEffect(() => {
    const dataSource: dataEntry[] = TraderListData
    setDataSource(dataSource)
  }, [])


  return (
      <Table<dataEntry>
          dataSource={dataSource}
          columns={columns}
          pagination={false}
          rowKey="id"
      />
  )
}

export default TraderList