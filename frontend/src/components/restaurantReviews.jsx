import RestaurantReviewCard from './restaurantReviewCard'

const RestaurantReviews = ({reviews}) => {


    if (!reviews || reviews.length === 0) {
        return <p className="text-center text-gray-500 my-4">No reviews available.</p>;
    }

    function formatToDDMMYYYY(isoString) {
        const date = new Date(isoString);

        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are 0-based
        const year = date.getFullYear();

        return `${day}-${month}-${year}`;
    }


    return (
        <div className='max-w-5xl mx-auto p-6 pt-0'>
            <h2 className="text-2xl font-bold mt-6 mb-4">Latest Reviews</h2>
            <div className="flex flex-wrap gap-12 max-w-5xl mx-auto p-6">
            {reviews.map((r, index) => (
                <RestaurantReviewCard
                key={index}
                name={r.reviewerName}
                review={{
                    "title" : r.title,
                    "content" : r.content
                }}
                date={formatToDDMMYYYY(r.reviewedAt)}
                rating={r.rating}
                />
            ))}
            </div>
        </div>
    )
}

export default RestaurantReviews;