import { useEffect,useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
import './App.css'

function App() {
  const [users, setUsers] = useState([])
const url = "http://localhost:8080/api/users";

  useEffect(()=>{
      fetch(url)
          .then(res=>res.json())
          .then(data => {
              setUsers(data);
          })
  },[]);

  return (
    <>
        <h1> Full Application </h1>
          <h2>Automated Deployment </h2>
        {/*{JSON.stringify(users)}*/}
    <table>
        <thead>
        <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Email</th>
            <th>Active</th>
        </tr>
        </thead>
        <tbody>
        {users.map(user => (
            <tr key={user.Id}>
                <td>{user.Id}</td>
                <td>{user.name}</td>
                <td>{user.email}</td>
                <td>{user.active ? "Active" : "Inactive"}</td>
            </tr>
        ))}
        </tbody>
    </table>
    </>
  )
}

export default App
