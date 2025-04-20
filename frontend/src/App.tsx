import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Main from "./pages/Main";
import Mypet from "./pages/Mypet";
import ProtectedRoute from "./components/ProtectedRoute";
import AddPet from "./pages/AddPet";
import EditPet from "./pages/EditPet";
import PetDetails from "./pages/PetDetails";
import PetBuyDetail from "./pages/PetBuyDetail";
import ThankYou from "./pages/ThankYou";
import Services from "./pages/Services";
import ProfilePage from "./pages/ProfilePage";
import CompleteProfilePage from "./pages/CompleteProfilePage";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        {/* <Route
          path="/main"
          element={
            <ProtectedRoute>
              <Main />
              <Mypet/>
            </ProtectedRoute>
          }
        /> */}
         <Route element={<ProtectedRoute />}>
            <Route path="/mypets" element={<Mypet />} />
            <Route path="/main" element={<Main/>} />
            <Route path="/addpet" element={<AddPet/>} />
            <Route path="/editpet/:id" element={<EditPet/>} />
            <Route path="/pets/:id" element={<PetDetails />} />
            <Route path="/buypets/:id" element={<PetBuyDetail />} />
            <Route path="/thankyou" element={<ThankYou />} />
            <Route path="/services" element={<Services />} />
            <Route path="/profile" element={< ProfilePage/>}/>
            <Route path="/complete-profile" element={<CompleteProfilePage />} />
          </Route>
      </Routes>
    </Router>
  );
}

export default App;
