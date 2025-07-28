const avatarColors = [
  'bg-blue-900',
  'bg-purple-900',
  'bg-gray-800',
  'bg-green-900',
  'bg-red-900',
  'bg-slate-800',
  'bg-indigo-900',
  'bg-emerald-900',
  'bg-teal-900',
  'bg-zinc-900',
];

const Avatar = ({ name }) => {
  if (!name) return null; // or a default avatar
  const index = Math.floor(Math.random() * avatarColors.length);
  const bgColor = avatarColors[index];

  return (
    <div
      className={`w-8 h-8 ${bgColor} text-white rounded-full flex items-center justify-center font-semibold`}
    >
      {name[0].toUpperCase()}
    </div>
  );
};

export default Avatar;
