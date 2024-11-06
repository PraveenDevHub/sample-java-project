import { createSlice } from '@reduxjs/toolkit';

const initialState = {
    token: null,
    role: null,
    name: null,
    userId: null,
    doctorId: null, // Add doctorId to the initial state
    patientId: null, // Add patientId to the initial state

};

const authSlice = createSlice({
    name: 'auth',
    initialState,
    reducers: {
        setAuth: (state, action) => {
            state.token = action.payload.token;
            state.role = action.payload.role;
            state.name = action.payload.name;
            state.userId = action.payload.userId;
            state.doctorId = action.payload.doctorId || null; // Set doctorId if present
            state.patientId = action.payload.patientId || null; // Set patientId if present
        },
        clearAuth: (state) => {
            state.token = null;
            state.role = null;
            state.name = null;
            state.userId = null;
            state.doctorId = null; // Clear doctorId
            state.patientId = null; // Clear patientId
            
            
        },
    },
});

export const { setAuth, clearAuth } = authSlice.actions;
export default authSlice.reducer;
