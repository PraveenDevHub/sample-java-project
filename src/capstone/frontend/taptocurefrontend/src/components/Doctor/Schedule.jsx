import React, { useState, useEffect } from "react";
import { Button, Form, Col, Row, Container, Card } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTrashAlt, faPlus, faSave } from "@fortawesome/free-solid-svg-icons";
import Navbar from "../Subcomponents/Navbar";
import Footer from "../Subcomponents/Footer";
import "../../css/Schedule.css";
import axiosInstance from "../../axiosConfig";
import { useSelector } from "react-redux";
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
  GET_DEFAULT_SCHEDULE_URL,
  UPDATE_SCHEDULE_URL,
  TOAST_OPTIONS,
  DIALOG_MESSAGES,
  BUTTON_LABELS,
  FORM_LABELS,
  TOAST_MESSAGES,
  ERROR_MESSAGES,
  TITLES,
  TEXTS,
  LOG_MESSAGES,
} from "./constants";

const Schedule = () => {
  const token = useSelector((state) => state.auth.token);
  const doctorId = useSelector((state) => state.auth.doctorId);
  const [availableDate, setAvailableDate] = useState("");
  const [timeBlocks, setTimeBlocks] = useState([
    {
      timeBlockStart: "",
      timeBlockEnd: "",
      slotDuration: "",
      availableTimeSlots: [],
    },
  ]);
  const [useDefaultSchedule, setUseDefaultSchedule] = useState(false);
  const [defaultScheduleExists, setDefaultScheduleExists] = useState(false);
  const [dayOfWeek, setDayOfWeek] = useState("");
  const [openDialog, setOpenDialog] = useState(false);
  const [dialogMessage, setDialogMessage] = useState("");

  useEffect(() => {
    if (availableDate) {
      const selectedDayOfWeek = new Date(availableDate).toLocaleDateString(
        "en-US",
        {
          weekday: "long",
        }
      );
      setDayOfWeek(selectedDayOfWeek);

      axiosInstance
        .get(GET_DEFAULT_SCHEDULE_URL(doctorId, selectedDayOfWeek), {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        .then((response) => {
          const defaultScheduleForDay = response.data;
          if (defaultScheduleForDay.length > 0) {
            setDefaultScheduleExists(true);
            if (useDefaultSchedule) {
              setTimeBlocks(
                defaultScheduleForDay.map((schedule) => ({
                  timeBlockStart: schedule.timeBlockStart,
                  timeBlockEnd: schedule.timeBlockEnd,
                  slotDuration: schedule.slotDuration,
                  availableTimeSlots: [],
                }))
              );
            }
          } else {
            setDefaultScheduleExists(false);
            setTimeBlocks([
              {
                timeBlockStart: "",
                timeBlockEnd: "",
                slotDuration: "",
                availableTimeSlots: [],
              },
            ]);
          }
        })
        .catch((error) => {
          console.error(ERROR_MESSAGES.FETCH_DEFAULT_SCHEDULE_ERROR, error);
          setDefaultScheduleExists(false);
        });
    }
  }, [availableDate, doctorId, token, useDefaultSchedule]);

  const generateTimeSlots = (start, end, duration) => {
    const slots = [];
    const formattedStart = start.slice(0, 5);
    const formattedEnd = end.slice(0, 5);
    let currentTime = new Date(`1970-01-01T${formattedStart}:00`);
    const endTime = new Date(`1970-01-01T${formattedEnd}:00`);
    console.log(
      TEXTS.GENERATING_TIME_SLOTS,
      formattedStart,
      TEXTS.TO,
      formattedEnd,
      TEXTS.WITH_DURATION,
      duration
    );

    if (currentTime >= endTime) {
      console.error(TEXTS.START_TIME_AFTER_END_TIME);
      return slots;
    }

    while (currentTime < endTime) {
      const timeSlot = currentTime.toTimeString().slice(0, 5);
      slots.push(timeSlot);
      currentTime = new Date(currentTime.getTime() + duration * 60000);
    }

    console.log(TEXTS.GENERATED_SLOTS, slots);
    return slots;
  };

  const addTimeBlock = () => {
    setTimeBlocks([
      ...timeBlocks,
      {
        timeBlockStart: "",
        timeBlockEnd: "",
        slotDuration: "",
        availableTimeSlots: [],
      },
    ]);
  };

  const removeTimeBlock = (index) => {
    const updatedBlocks = timeBlocks.filter((_, i) => i !== index);
    setTimeBlocks(updatedBlocks);
  };

  const handleInputChange = (index, field, value) => {
    const updatedBlocks = [...timeBlocks];
    updatedBlocks[index][field] = value;
    setTimeBlocks(updatedBlocks);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const currentDateTime = new Date();
    const selectedDate = new Date(availableDate);

    const timeBlocksWithSlots = timeBlocks
      .map((block) => {
        const start = new Date(`1970-01-01T${block.timeBlockStart}:00`);
        const end = new Date(`1970-01-01T${block.timeBlockEnd}:00`);
        const duration = block.slotDuration;

        // Check if slot duration exceeds the time block duration
        if (end - start < duration * 60000) {
          toast.error(TOAST_MESSAGES.INVALID_SLOT_DURATION, TOAST_OPTIONS);
          return null;
        }

        // Check if the schedule time is in the past for the current day
        if (
          selectedDate.toDateString() === currentDateTime.toDateString() &&
          start.getHours() < currentDateTime.getHours() &&
          start.getMinutes() < currentDateTime.getMinutes()
        ) {
          toast.error(TOAST_MESSAGES.INVALID_PAST_TIME, TOAST_OPTIONS);
          return null;
        }

        const slots = generateTimeSlots(
          block.timeBlockStart,
          block.timeBlockEnd,
          block.slotDuration
        );

        if (slots.length === 0) {
          toast.error(TOAST_MESSAGES.INVALID_TIME_BLOCK, TOAST_OPTIONS);
          return null;
        }

        console.log(TEXTS.TIME_BLOCK_WITH_SLOTS, {
          ...block,
          availableTimeSlots: slots,
        });

        return {
          ...block,
          availableTimeSlots: slots,
        };
      })
      .filter((block) => block !== null);

    if (timeBlocksWithSlots.length === 0) {
      return;
    }

    for (const block of timeBlocksWithSlots) {
      const requestData = {
        doctor: { doctorId },
        availableDate,
        timeBlockStart: block.timeBlockStart,
        timeBlockEnd: block.timeBlockEnd,
        slotDuration: block.slotDuration,
        availableTimeSlots: JSON.stringify(block.availableTimeSlots),
      };

      try {
        const response = await axiosInstance.post(
          UPDATE_SCHEDULE_URL,
          requestData,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          }
        );
        console.log(TEXTS.RESPONSE, response.data);
      } catch (error) {
        console.error(ERROR_MESSAGES.UPDATE_AVAILABILITY_ERROR, error);
        toast.error(TOAST_MESSAGES.UPDATE_AVAILABILITY_FAILURE, TOAST_OPTIONS);
        return;
      }
    }

    toast.success(TOAST_MESSAGES.UPDATE_AVAILABILITY_SUCCESS, TOAST_OPTIONS);
  };


  return (
    <div>
      <Navbar />
      <div className="schedule-container">
        <Container className="mt-5 p-4 shadow-lg rounded">
          <h2 className="text-center mb-4">
            {TITLES.SET_AVAILABILITY_SCHEDULE}
          </h2>
          <Form onSubmit={handleSubmit}>
            <Form.Group
              as={Row}
              controlId="availableDate"
              className="justify-content-center"
            >
              <Form.Label column sm={2.5} className="text-center">
                {FORM_LABELS.AVAILABLE_DATE}
              </Form.Label>
              <Col sm={5}>
                <Form.Control
                  type="date"
                  value={availableDate}
                  onChange={(e) => setAvailableDate(e.target.value)}
                  className="form-control"
                  min={new Date().toISOString().split("T")[0]}
                  required
                />
              </Col>
              {defaultScheduleExists && (
                <Col sm={2}>
                  <Form.Check
                    type="switch"
                    id="custom-switch"
                    label={`${FORM_LABELS.USE_DEFAULT_SCHEDULE}`}
                    checked={useDefaultSchedule}
                    onChange={(e) => {
                      setUseDefaultSchedule(e.target.checked);
                      if (!e.target.checked) {
                        setTimeBlocks([
                          {
                            timeBlockStart: "",
                            timeBlockEnd: "",
                            slotDuration: "",
                            availableTimeSlots: [],
                          },
                        ]);
                      }
                    }}
                    className="custom-switch"
                  />
                </Col>
              )}
            </Form.Group>

            {timeBlocks.map((block, index) => (
              <Card className="mb-4 mt-4" key={index}>
                <Card.Body>
                  <h5 className="text-center">{`${FORM_LABELS.TIME_BLOCK} ${index + 1
                    }`}</h5>
                  <Form.Group
                    as={Row}
                    controlId={`timeBlockStart-${index}`}
                    className="justify-content-center"
                  >
                    <Form.Label column sm={3} className="text-center">
                      {FORM_LABELS.START_TIME}
                    </Form.Label>
                    <Col sm={6}>
                      <Form.Control
                        type="time"
                        value={block.timeBlockStart}
                        onChange={(e) =>
                          handleInputChange(
                            index,
                            "timeBlockStart",
                            e.target.value
                          )
                        }
                        required
                      />
                    </Col>
                  </Form.Group>
                  <Form.Group
                    as={Row}
                    controlId={`timeBlockEnd-${index}`}
                    className="justify-content-center"
                  >
                    <Form.Label column sm={3} className="text-center">
                      {FORM_LABELS.END_TIME}
                    </Form.Label>
                    <Col sm={6}>
                      <Form.Control
                        type="time"
                        value={block.timeBlockEnd}
                        onChange={(e) =>
                          handleInputChange(
                            index,
                            "timeBlockEnd",
                            e.target.value
                          )
                        }
                        required
                      />
                    </Col>
                  </Form.Group>
                  <Form.Group
                    as={Row}
                    controlId={`slotDuration-${index}`}
                    className="justify-content-center"
                  >
                    <Form.Label column sm={3} className="text-center">
                      {FORM_LABELS.SLOT_DURATION}
                    </Form.Label>
                    <Col sm={6}>
                      <Form.Control
                        type="number"
                        value={block.slotDuration}
                        onChange={(e) =>
                          handleInputChange(
                            index,
                            "slotDuration",
                            e.target.value
                          )
                        }
                        placeholder="Enter slot duration in minutes"
                        required
                      />
                    </Col>
                  </Form.Group>
                  {index > 0 && (
                    <div className="d-flex justify-content-center">
                      <Button
                        variant="danger"
                        className="mt-2 mb-3 remove-time-block-btn"
                        onClick={() => {
                          setDialogMessage(
                            DIALOG_MESSAGES.REMOVE_TIME_BLOCK_CONFIRM
                          );
                          setOpenDialog(true);
                          setTimeBlocks((prevBlocks) =>
                            prevBlocks.filter((_, i) => i !== index)
                          );
                        }}
                      >
                        <FontAwesomeIcon icon={faTrashAlt} />
                      </Button>
                    </div>
                  )}
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
              <Button variant="primary" type="submit" className="save-btn">
                <FontAwesomeIcon icon={faSave} />
              </Button>
            </div>
          </Form>
        </Container>
      </div>
      <Footer />
      <ToastContainer />
      <Dialog open={openDialog} onClose={() => setOpenDialog(false)}>
        <DialogTitle>{TITLES.CONFIRM_ACTION}</DialogTitle>
        <DialogContent>
          <DialogContentText>{dialogMessage}</DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)} color="primary">
            {BUTTON_LABELS.NO}
          </Button>
          <Button
            onClick={() => {
              setOpenDialog(false);
              // Perform the action here
            }}
            color="primary"
          >
            {BUTTON_LABELS.YES}
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
};

export default Schedule;
