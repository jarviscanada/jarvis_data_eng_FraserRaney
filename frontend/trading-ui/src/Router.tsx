import {BrowserRouter, Route, Routes} from 'react-router-dom';
import Dashboard from './page/Dashboard/Dashboard';

export default function Router() {
  return (
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Dashboard/>}/>
          <Route path="/dashboard" element={<Dashboard/>}/>
        </Routes>
      </BrowserRouter>
  );
}
