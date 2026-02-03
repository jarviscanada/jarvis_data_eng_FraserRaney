//import React from 'react';
import {Popconfirm, Table} from 'antd';
//import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import './TraderList.scss'
//import { useState, useEffect } from 'react'
import TraderListData from './TraderListData.json'
import type {ColumnsType} from 'antd/es/table';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faTrashAlt as deleteIcon} from '@fortawesome/free-solid-svg-icons';

export type dataEntry = {
  key: string;
  id: number;
  firstName: string;
  lastName: string;
  dob: string;
  country: string;
  email: string;
  amount: number;
}

type TraderListProps = {
  onTraderDeleteClick: (id: number) => void;
};

function TraderList(props: TraderListProps) {
  const columns: ColumnsType<dataEntry> = [
    {
      title: 'First Name',
      dataIndex: 'firstName',
      key: 'firstName',
      sorter: (a, b) => a.firstName.localeCompare(b.firstName),
    },
    {
      title: 'Last Name',
      dataIndex: 'lastName',
      key: 'lastName',
      sorter: (a, b) => a.lastName.localeCompare(b.lastName),
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
      sorter: (a, b) => new Date(a.dob).getTime() - new Date(b.dob).getTime()
    },
    {
      title: 'Country',
      dataIndex: 'country',
      key: 'country',
    },
    {
      title: 'Actions',
      dataIndex: 'actions',
      key: 'actions',
      render: (_, record) => (
          <Popconfirm
              title="Delete trader?"
              onConfirm={() => props.onTraderDeleteClick(record.id)}
          >
            <div className="trader-delete-icon">
              <FontAwesomeIcon icon={deleteIcon}/>
            </div>
          </Popconfirm>
      ),
    },
  ];
  // Initialization of columns for tabl
  const dataSource: dataEntry[] = TraderListData
  //const [tableColumns, setTableColumns] = useState(columns)
  //const [dataSource, setDataSource] = useState<dataEntry[]>([])


  /*
  useEffect(() => {
    //const dataSource: dataEntry[] = TraderListData
    //setDataSource(dataSource)
  }, [])
  */

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