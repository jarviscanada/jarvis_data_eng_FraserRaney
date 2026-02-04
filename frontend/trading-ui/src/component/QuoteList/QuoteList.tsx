//import React from 'react';
import {Table} from 'antd';
//import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import './QuoteList.scss'
//import { useState, useEffect } from 'react'
//import QuoteListData from './QuoteListData.json'
import type {ColumnsType} from 'antd/es/table';

export type Quote = {
  ticker: string;
  lastPrice: number;
  high: number;
  low: number;
  open: number;
  close: number;
}

type QuoteListProps = {
  quotes?: Quote[];
};

function QuoteList(props: QuoteListProps) {
  const columns: ColumnsType<Quote> = [
    {
      title: 'Ticker',
      dataIndex: 'ticker',
      key: 'ticker',
      sorter: (a, b) => a.ticker.localeCompare(b.ticker),
    },
    {
      title: 'Last Price',
      dataIndex: 'lastPrice',
      key: 'lastPrice',
      sorter: (a, b) => a.lastPrice - b.lastPrice,
    },
    {
      title: 'High',
      dataIndex: 'high',
      key: 'high',
    },
    {
      title: 'Low',
      dataIndex: 'low',
      key: 'low',
      sorter: (a, b) => a.low - b.low,
    },
    {
      title: 'Open',
      dataIndex: 'open',
      key: 'open',
    },
    {
      title: 'Close',
      dataIndex: 'close',
      key: 'close',
    }
  ];
  // Initialization of columns for tabl
  const dataSource: Quote[] = props.quotes || [];

  return (
      <Table<Quote>
          dataSource={dataSource}
          columns={columns}
          pagination={false}
          rowKey="ticker"
      />
  )
}

export default QuoteList