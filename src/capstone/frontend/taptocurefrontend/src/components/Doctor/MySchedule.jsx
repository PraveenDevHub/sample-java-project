import React, { useState, useEffect } from "react";
import { Button, Form, Col, Row, Container, Card } from "react-bootstrap";
import axiosInstance from "../../axiosConfig";
import { useSelector } from "react-redux";
import Navbar from "../Subcomponents/Navbar";
import Footer from "../Subcomponents/Footer";
import "../../css/defaultschedule.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTrashAlt, faPlus, faSave } from "@fortawesome/free-solid-svg-icons";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import {
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
} from "@mui/material";
import {
    SAVE_DEFAULT_SCHEDULE_URL,
    DAYS_OF_WEEK,
    TOAST_OPTIONS,
    DIALOG_MESSAGES,
    BUTTON_LABELS,
    FORM_LABELS,
    TOAST_MESSAGES,
} from "./constants";

const DefaultSchedule = () => {
    const token = useSelector((state) => state.auth.token);
    const doctorId = useSelector((state) => state.auth.doctorId);
    const [defaultSchedule, setDefaultSchedule] = useState({});
    const [selectedDay, setSelectedDay] = useState("");
    const [timeBlocks, setTimeBlocks] = useState([]);
    const [openDialog, setOpenDialog] = useState(false);
    const [dialogMessage, setDialogMessage] = useState("");
    const [dialogAction, setDialogAction] = useState(null);

    const generateTimeSlots = (start, end, duration) => {
        const slots = [];
        let currentTime = new Date(`1970-01-01T${start}:00`);
        const endTime = new Date(`1970-01-01T${end}:00`);
        while (currentTime < endTime) {
            slots.push(currentTime.toTimeString().slice(0, 5));
            currentTime = new Date(currentTime.getTime() + duration * 60000);
        }
        return slots;
    };

    useEffect(() => { }, []);

    const handleDayChange = (day) => {
        setSelectedDay(day);
        setTimeBlocks(
            defaultSchedule[day] || [{ start: "", end: "", duration: "" }]
        );
    };

    const handleTimeBlockChange = (index, field, value) => {
        const updatedBlocks = [...timeBlocks];
        updatedBlocks[index][field] = value;
        setTimeBlocks(updatedBlocks);
    };

    const addTimeBlock = () => {
        setTimeBlocks([...timeBlocks, { start: "", end: "", duration: "" }]);
    };

    const removeTimeBlock = (index) => {
        setDialogMessage(DIALOG_MESSAGES.REMOVE_TIME_BLOCK);
        setDialogAction(() => () => {
            const updatedBlocks = timeBlocks.filter((_, i) => i !== index);
            setTimeBlocks(updatedBlocks);
        });
        setOpenDialog(true);
    };

    const handleSave = async () => {
        const timeBlocksWithSlots = timeBlocks
            .map((block) => {
                const start = new Date(`1970-01-01T${block.start}:00`);
                const end = new Date(`1970-01-01T${block.end}:00`);
                const duration = block.duration;

                // Validate start time is not after end time
                if (start >= end) {
                    toast.error(TOAST_MESSAGES.INVALID_TIME_BLOCK, TOAST_OPTIONS);
                    return null;
                }

                // Validate slot duration does not exceed the time block duration
                if (end - start < duration * 60000) {
                    toast.error(TOAST_MESSAGES.INVALID_SLOT_DURATION, TOAST_OPTIONS);
                    return null;
                }

                return {
                    ...block,
                    availableTimeSlots: generateTimeSlots(
                        block.start,
                        block.end,
                        block.duration
                    ),
                };
            })
            .filter((block) => block !== null);

        if (timeBlocksWithSlots.length === 0) {
            return;
        }

        const updatedSchedule = {
            ...defaultSchedule,
            [selectedDay]: timeBlocksWithSlots,
        };

        console.log("Time blocks with slots:", timeBlocksWithSlots);

        try {
            await axiosInstance.post(
                SAVE_DEFAULT_SCHEDULE_URL,
                {
                    doctorId,
                    defaultSchedule: updatedSchedule,
                },
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                }
            );
            toast.success(TOAST_MESSAGES.SAVE_SUCCESS, TOAST_OPTIONS);
        } catch (error) {
            console.error("Error saving default schedule:", error);
            toast.error(TOAST_MESSAGES.SAVE_FAILURE, TOAST_OPTIONS);
        }
    };

    return (
        <div>
            <Navbar />
            <div className="defaultschedule-container">
                <Container>
                    <h2>Set Default Schedule</h2>
                    <Form>
                        <Form.Group as={Row} className="justify-content-center mb-3">
                            <Col sm={10} md={8} lg={8}>
                                <Form.Control
                                    as="select"
                                    value={selectedDay}
                                    onChange={(e) => handleDayChange(e.target.value)}
                                >
                                    <option value="">{FORM_LABELS.SELECT_DAY}</option>
                                    {DAYS_OF_WEEK.map((day) => (
                                        <option key={day} value={day}>
                                            {day}
                                        </option>
                                    ))}
                                </Form.Control>
                            </Col>
                        </Form.Group>

                        {selectedDay &&
                            timeBlocks.map((block, index) => (
                                <Card key={index} className="mb-4">
                                    <Card.Body>
                                        <h5 className="text-center">{`${FORM_LABELS.TIME_BLOCK} ${index + 1
                                            }`}</h5>
                                        <Form.Group
                                            as={Row}
                                            controlId={`timeBlockStart-${index}`}
                                            className="justify-content-center"
                                        >
                                            <Form.Label column sm={3}>
                                                {FORM_LABELS.START_TIME}
                                            </Form.Label>
                                            <Col sm={6}>
                                                <Form.Control
                                                    type="time"
                                                    value={block.start}
                                                    onChange={(e) =>
                                                        handleTimeBlockChange(
                                                            index,
                                                            "start",
                                                            e.target.value
                                                        )
                                                    }
                                                />
                                            </Col>
                                        </Form.Group>
                                        <Form.Group
                                            as={Row}
                                            controlId={`timeBlockEnd-${index}`}
                                            className="justify-content-center"
                                        >
                                            <Form.Label column sm={3}>
                                                {FORM_LABELS.END_TIME}
                                            </Form.Label>
                                            <Col sm={6}>
                                                <Form.Control
                                                    type="time"
                                                    value={block.end}
                                                    onChange={(e) =>
                                                        handleTimeBlockChange(index, "end", e.target.value)
                                                    }
                                                />
                                            </Col>
                                        </Form.Group>
                                        <Form.Group
                                            as={Row}
                                            controlId={`slotDuration-${index}`}
                                            className="justify-content-center"
                                        >
                                            <Form.Label column sm={3}>
                                                {FORM_LABELS.SLOT_DURATION}
                                            </Form.Label>
                                            <Col sm={6}>
                                                <Form.Control
                                                    type="number"
                                                    value={block.duration}
                                                    onChange={(e) =>
                                                        handleTimeBlockChange(
                                                            index,
                                                            "duration",
                                                            e.target.value
                                                        )
                                                    }
                                                />
                                            </Col>
                                        </Form.Group>
                                        <Button
                                            variant="danger"
                                            className="remove-time-block-btn"
                                            onClick={() => removeTimeBlock(index)}
                                        >
                                            <FontAwesomeIcon icon={faTrashAlt} />
                                        </Button>
                                    </Card.Body>
                                </Card>
                            ))}

                        <div className="d-flex justify-content-center mb-4 icon-buttons">
                            <Button
                                variant="success"
                                className="me-3 add-time-block-btn"
                                onClick={addTimeBlock}
                            >
                                <FontAwesomeIcon icon={faPlus} />
                            </Button>
                            <Button
                                variant="primary"
                                className="save-btn"
                                onClick={handleSave}
                            >
                                <FontAwesomeIcon icon={faSave} />
                            </Button>
                        </div>
                    </Form>
                </Container>
            </div>
            <Footer />
            <ToastContainer />
            <Dialog open={openDialog} onClose={() => setOpenDialog(false)}>
                <DialogTitle>Confirm Action</DialogTitle>
                <DialogContent>
                    <DialogContentText>{dialogMessage}</DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setOpenDialog(false)} color="primary">
                        No
                    </Button>
                    <Button
                        onClick={() => {
                            setOpenDialog(false);
                            dialogAction();
                        }}
                        color="primary"
                    >
                        Yes
                    </Button>
                </DialogActions>
            </Dialog>
        </div>
    );
};

export default DefaultSchedule;
