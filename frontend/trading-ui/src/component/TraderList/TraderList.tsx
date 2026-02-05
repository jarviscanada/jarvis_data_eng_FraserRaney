import {Popconfirm, Table} from 'antd';
import './TraderList.scss'
import type {ColumnsType} from 'antd/es/table';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faTrashAlt as deleteIcon} from '@fortawesome/free-solid-svg-icons';
import {useNavigate} from "react-router-dom";

export type Trader = {
  key: string;
  id: number;
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  country: string;
  email: string;
}

type TraderListProps = {
  onTraderDeleteClick: (id: number) => void;
  traders: Trader[];
};

function TraderList(props: TraderListProps) {
  const navigate = useNavigate();
  const columns: ColumnsType<Trader> = [
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
      dataIndex: 'dateOfBirth',
      key: 'dateOfBirth',
      sorter: (a, b) => new Date(a.dateOfBirth).getTime() - new Date(b.dateOfBirth).getTime()
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
            <div className="trader-delete-icon"
                 onClick={(e) => e.stopPropagation()}
            >
              <FontAwesomeIcon icon={deleteIcon}/>
            </div>
          </Popconfirm>
      ),
    },
  ];

  const dataSource: Trader[] = props.traders

  return (
      <Table<Trader>
          dataSource={dataSource}
          columns={columns}
          pagination={false}
          rowKey="id"
          onRow={(record) => ({
            onClick: () => {
              navigate(`/trader/${record.id}`);
            },
          })}
      />
  )
}

export default TraderList