const Modal = ({ isOpen, onClose }) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black/25 flex justify-center items-center z-50">
      <div className="bg-white p-8 rounded-lg shadow-lg w-[90%] max-w-md text-center">
        <p className="text-xl font-bold mb-6">
          You will get an email confirmation. Kindly check your inbox.
        </p>
        <button
          onClick={onClose}
          className="px-6 py-2 bg-black text-white rounded hover:bg-gray-800"
        >
          Okay
        </button>
      </div>
    </div>
  );
};

export default Modal;
